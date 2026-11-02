/**
 * SmartCity360 – Energy page JS
 * - Stream lab: layer chips, live pipeline preview, token field
 * - Meter filter + search, summary numbers, gauges
 * - Modals: add reading, add meter
 * - kWh bar chart (Chart.js)
 */
'use strict';

(function () {
  const $  = (sel, root = document) => root.querySelector(sel);
  const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
  const state = { status: 'all', query: '' };

  /* ── Summary numbers + gauges ─────────────────────────────── */
  const cards = () => $$('[data-meter-card]');

  function fillSummary() {
    const all = cards();
    const values = all.map(c => parseFloat(c.dataset.kwh) || 0);
    const sum = values.reduce((a, b) => a + b, 0);
    const max = Math.max(1, ...values);

    setText('statTotal', all.length);
    setText('statActive', all.filter(c => c.dataset.active === 'true').length);
    setText('statKwh', sum.toLocaleString(undefined, { maximumFractionDigits: 1 }));

    all.forEach((card, i) => {
      const bar = $('.js-gauge', card);
      if (bar) requestAnimationFrame(() => { bar.style.width = Math.round((values[i] / max) * 100) + '%'; });
    });
  }

  function setText(id, value) {
    const el = document.getElementById(id);
    if (el) el.textContent = value;
  }

  /* ── Filter + search ──────────────────────────────────────── */
  const ON  = ['bg-white', 'text-slate-900', 'shadow-sm'];
  const OFF = ['text-slate-500', 'hover:text-slate-900'];

  function applyFilters() {
    let shown = 0;
    cards().forEach(card => {
      const isActive = card.dataset.active === 'true';
      const okStatus = state.status === 'all' ||
          (state.status === 'active' && isActive) ||
          (state.status === 'inactive' && !isActive);
      const okQuery = !state.query || (card.dataset.search || '').toLowerCase().includes(state.query);
      const visible = okStatus && okQuery;
      card.classList.toggle('hidden', !visible);
      if (visible) shown++;
    });
    const none = document.getElementById('noMatch');
    if (none) none.classList.toggle('hidden', shown !== 0 || cards().length === 0);
  }

  function setFilter(status) {
    state.status = status;
    $$('[data-filter]').forEach(btn => {
      const on = btn.dataset.filter === status;
      ON.forEach(c => btn.classList.toggle(c, on));
      OFF.forEach(c => btn.classList.toggle(c, !on));
    });
    applyFilters();
  }

  /* ── Modals ───────────────────────────────────────────────── */
  function openModal(id) {
    const m = document.getElementById(id);
    if (!m) return;
    m.classList.remove('hidden');
    m.classList.add('flex');
    document.body.style.overflow = 'hidden';
    const first = $('input:not([type="hidden"])', m);
    if (first) first.focus();
  }

  function closeModals() {
    $$('[role="dialog"]').forEach(m => { m.classList.add('hidden'); m.classList.remove('flex'); });
    document.body.style.overflow = '';
  }

  function openReadingModal(meterId, meterCode) {
    const m = document.getElementById('addReadingModal');
    if (!m) return;
    $('form', m).reset();
    document.getElementById('readingMeterId').value = meterId;
    document.getElementById('readingMeterCode').textContent = meterCode || ('Meter #' + meterId);
    openModal('addReadingModal');
  }

  /* ── Stream lab ───────────────────────────────────────────── */
  function syncPipeline() {
    $$('#labForm input[data-layer]').forEach(box => {
      const node = $('[data-node="' + box.dataset.layer + '"]');
      if (node) node.classList.toggle('hidden', !box.checked);
    });

    const auth = $('#labForm input[data-layer="Authentication"]');
    const row = document.getElementById('tokenRow');
    const token = document.getElementById('labToken');
    if (auth && row && token) {
      row.classList.toggle('hidden', !auth.checked);
      row.classList.toggle('flex', auth.checked);
      token.required = auth.checked;
    }
  }

  function initLab() {
    const form = document.getElementById('labForm');
    if (!form) return;

    $$('input[data-layer]', form).forEach(box => box.addEventListener('change', syncPipeline));
    syncPipeline();

    const fill = document.getElementById('fillToken');
    if (fill) fill.addEventListener('click', () => {
      const t = document.getElementById('labToken');
      t.value = fill.dataset.token;
      t.focus();
    });

    // Ctrl + Enter sends the form
    const payload = document.getElementById('labPayload');
    if (payload) payload.addEventListener('keydown', e => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') form.requestSubmit();
    });

    form.addEventListener('submit', () => {
      const btn = document.getElementById('labRun');
      // disable after the browser has collected the form data
      if (btn) setTimeout(() => { btn.disabled = true; btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Running…'; }, 0);
    });

    // "Use in lab" button on a meter card (නිවැරදි කරන ලද කොටස)
    $$('.js-use-in-lab').forEach(btn => btn.addEventListener('click', () => {
      const select = document.getElementById('labMeter');
      if (select) select.value = btn.dataset.meterId;

      // අදාළ මීටරයේ දත්ත textarea එකට ස්වයංක්‍රීයව ඇතුළත් කිරීම
      if (payload && btn.dataset.payload) {
        payload.value = btn.dataset.payload;
      }

      document.getElementById('lab').scrollIntoView({ behavior: 'smooth', block: 'center' });
      if (payload) payload.focus({ preventScroll: true });
    }));

    // Copy button in the result
    $$('.js-copy').forEach(btn => btn.addEventListener('click', () => {
      const target = document.getElementById(btn.dataset.target);
      if (!target || !navigator.clipboard) return;
      navigator.clipboard.writeText(target.textContent).then(() => {
        const old = btn.textContent;
        btn.textContent = 'Copied';
        setTimeout(() => { btn.textContent = old; }, 1500);
      });
    }));

    // show the result when the page loads after a run
    const result = document.getElementById('labResult');
    if (result) result.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }

  /* ── Chart ────────────────────────────────────────────────── */
  function drawChart() {
    const dataEl = document.getElementById('energyChartData');
    const canvas = document.getElementById('energyChart');
    if (!dataEl || !canvas || !window.Chart) return;

    try {
      const data = JSON.parse(dataEl.textContent);
      const grad = canvas.getContext('2d').createLinearGradient(0, 0, 0, 280);
      grad.addColorStop(0, '#818cf8');
      grad.addColorStop(1, '#8b5cf6');
      Chart.defaults.color = '#64748b';

      new Chart(canvas, {
        type: 'bar',
        data: {
          labels: data.labels || [],
          datasets: [{ label: 'kWh', data: data.kwh || [], backgroundColor: grad, borderRadius: 10, borderSkipped: false, maxBarThickness: 44 }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false },
            tooltip: { backgroundColor: '#0f172a', padding: 10, cornerRadius: 10, displayColors: false, callbacks: { label: c => c.parsed.y + ' kWh' } }
          },
          scales: {
            y: { beginAtZero: true, border: { display: false }, grid: { color: '#eef1f6' }, ticks: { callback: v => v + ' kWh' } },
            x: { grid: { display: false }, border: { display: false } }
          }
        }
      });
    } catch (e) {
      console.error('Energy chart error:', e);
    }
  }

  /* ── Start ────────────────────────────────────────────────── */
  document.addEventListener('DOMContentLoaded', () => {
    fillSummary();
    drawChart();
    setFilter('all');
    initLab();

    $$('[data-filter]').forEach(btn => btn.addEventListener('click', () => setFilter(btn.dataset.filter)));

    const search = document.getElementById('meterSearch');
    if (search) search.addEventListener('input', () => {
      state.query = search.value.trim().toLowerCase();
      applyFilters();
    });

    $$('.js-add-reading').forEach(btn => btn.addEventListener('click', () => openReadingModal(btn.dataset.meterId, btn.dataset.meterCode)));

    const addMeter = document.getElementById('openAddMeter');
    if (addMeter) addMeter.addEventListener('click', () => {
      $('form', document.getElementById('addMeterModal')).reset();
      openModal('addMeterModal');
    });

    $$('[data-modal-close]').forEach(el => el.addEventListener('click', closeModals));
    document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModals(); });
  });
})();