'use strict';

/* Draws whichever dashboard charts exist on the page (supervisor: trend; city manager: trend + donut). */
(function () {
  if (window.__scDashboardInit) return;
  window.__scDashboardInit = true;

  function readJson(el, key, fallback) {
    try { return JSON.parse(el.dataset[key]); } catch (e) { return fallback; }
  }

  function init() {
    if (!window.Chart) { console.error('Chart.js did not load'); return; }
    Chart.defaults.font.family = 'Manrope, system-ui, sans-serif';

    var trendEl = document.getElementById('complaintTrendChart');
    if (trendEl) {
      new Chart(trendEl, {
        type: 'line',
        data: {
          labels: readJson(trendEl, 'labels', []),
          datasets: [
            { label: 'Submitted', data: readJson(trendEl, 'submitted', []),
              borderColor: '#6366f1', backgroundColor: 'rgba(99,102,241,0.08)',
              fill: true, tension: 0.35, borderWidth: 2, pointRadius: 3 },
            { label: 'Resolved', data: readJson(trendEl, 'resolved', []),
              borderColor: '#10b981', backgroundColor: 'rgba(16,185,129,0.08)',
              fill: true, tension: 0.35, borderWidth: 2, pointRadius: 3 }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: { mode: 'index', intersect: false },
          plugins: { legend: { position: 'top', align: 'end', labels: { boxWidth: 10, font: { size: 11 } } } },
          scales: {
            y: { beginAtZero: true, ticks: { precision: 0, font: { size: 10 } }, grid: { color: '#f1f5f9' } },
            x: { grid: { display: false }, ticks: { font: { size: 10 } } }
          }
        }
      });
    }

    var donutEl = document.getElementById('complaintDonut');
    if (donutEl) {
      var values = ['open', 'escalated', 'resolved', 'unresolved'].map(function (k) {
        return parseInt(donutEl.dataset[k] || '0', 10);
      });
      var total = values.reduce(function (a, b) { return a + b; }, 0);

      new Chart(donutEl, {
        type: 'doughnut',
        data: {
          labels: total ? ['Open', 'Escalated', 'Resolved', 'Unresolved'] : ['No complaints yet'],
          datasets: [{
            data: total ? values : [1],
            backgroundColor: total ? ['#6366f1', '#f59e0b', '#10b981', '#f43f5e'] : ['#e2e8f0'],
            borderWidth: 0,
            hoverOffset: total ? 4 : 0
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: '70%',
          plugins: {
            legend: { position: 'bottom', labels: { padding: 12, boxWidth: 8, font: { size: 11 } } },
            tooltip: { enabled: total > 0 }
          }
        }
      });
    }
  }

  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init);
  else init();
})();