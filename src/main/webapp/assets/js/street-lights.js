/**
 * SmartCity360 – Street Lights JS
 * - Filter cards by zone / on-off status
 * - Toggle light via AJAX
 * - Stats summary counter
 */
'use strict';

function getCsrfToken() {
  const meta = document.querySelector('meta[name="csrf-token"]');
  return meta ? meta.getAttribute('content') : '';
}

function filterLights() {
  const zoneFilter   = document.getElementById('zoneFilter');
  const statusFilter = document.getElementById('statusFilter');
  const zoneId = zoneFilter   ? zoneFilter.value   : 'all';
  const status = statusFilter ? statusFilter.value : 'all';

  document.querySelectorAll('.sc-light-card').forEach(card => {
    let show = true;
    if (zoneId !== 'all' && card.dataset.zoneId !== zoneId) show = false;
    if (status === 'on'  && card.dataset.isOn !== 'true')   show = false;
    if (status === 'off' && card.dataset.isOn !== 'false')  show = false;
    card.style.display = show ? '' : 'none';
  });

  updateStats();
}

function toggleLight(lightId, currentOn, ctxPath) {
  const action = currentOn ? 'off' : 'on';
  const card   = document.getElementById('light-' + lightId);
  if (card) { card.style.opacity = '0.5'; card.style.pointerEvents = 'none'; }

  const body = new URLSearchParams({ id: lightId, action, _csrf: getCsrfToken() });
  fetch(ctxPath + '/app/street-lights', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: body.toString()
  })
  .then(resp => {
    if (resp.ok || resp.redirected) {
      window.location.reload();
    } else {
      if (typeof Toast !== 'undefined') Toast.show('Failed to toggle light. Please try again.', 'error');
      else alert('Failed to toggle light.');
      if (card) { card.style.opacity = '1'; card.style.pointerEvents = ''; }
    }
  })
  .catch(() => {
    if (typeof Toast !== 'undefined') Toast.show('Network error. Please try again.', 'error');
    if (card) { card.style.opacity = '1'; card.style.pointerEvents = ''; }
  });
}

function updateStats() {
  const visible = [...document.querySelectorAll('.sc-light-card')].filter(c => c.style.display !== 'none');
  const on = visible.filter(c => c.dataset.isOn === 'true').length;
  const statsEl = document.getElementById('lightStats');
  if (statsEl) statsEl.textContent = `${on} on · ${visible.length - on} off · ${visible.length} shown`;
}

document.addEventListener('DOMContentLoaded', () => {
  const zoneFilter   = document.getElementById('zoneFilter');
  const statusFilter = document.getElementById('statusFilter');

  if (zoneFilter)   zoneFilter.addEventListener('change', filterLights);
  if (statusFilter) statusFilter.addEventListener('change', filterLights);

  // Populate zone dropdown from rendered cards
  if (zoneFilter) {
    const seen = new Set();
    document.querySelectorAll('.sc-light-card[data-zone-id]').forEach(card => {
      const id   = card.dataset.zoneId;
      const name = card.dataset.zoneName;
      if (id && name && !seen.has(id)) {
        seen.add(id);
        const opt = document.createElement('option');
        opt.value = id;
        opt.textContent = name;
        zoneFilter.appendChild(opt);
      }
    });
  }

  updateStats();
});
