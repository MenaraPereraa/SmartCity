/**
 * SmartCity360 — Main JavaScript
 * Handles: AJAX helpers, zone tree rendering, street light toggles,
 * policy expression validator, energy chart, CSRF injection.
 */

'use strict';

/* =========================================================
   1. CSRF Helper
   ========================================================= */
function getCsrfToken() {
    const meta = document.querySelector('meta[name="csrf-token"]');
    return meta ? meta.getAttribute('content') : '';
}

/**
 * POST helper that automatically includes the CSRF token.
 */
async function scPost(url, params) {
    const body = new URLSearchParams(params);
    body.set('_csrf', getCsrfToken());
    const resp = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: body.toString()
    });
    return resp;
}

/* =========================================================
   2. Zone Tree
   ========================================================= */
const ZoneTree = (() => {
    let selectedZoneId = null;

    function render(zones, containerId) {
        const container = document.getElementById(containerId);
        if (!container || !zones || zones.length === 0) return;

        // Build a map of id -> children
        const childMap = {};
        const roots = [];
        zones.forEach(z => {
            childMap[z.id] = [];
        });
        zones.forEach(z => {
            if (z.parentId && childMap[z.parentId] !== undefined) {
                childMap[z.parentId].push(z);
            } else {
                roots.push(z);
            }
        });

        const ul = buildList(roots, childMap);
        container.innerHTML = '';
        container.appendChild(ul);
    }

    function buildList(nodes, childMap) {
        const ul = document.createElement('ul');
        ul.className = 'sc-zone-tree';
        nodes.forEach(z => {
            const li = document.createElement('li');
            const item = document.createElement('div');
            item.className = 'sc-zone-item';
            item.dataset.id = z.id;

            const badge = document.createElement('span');
            badge.className = 'sc-zone-badge ' + z.level.toLowerCase();
            badge.textContent = z.level;

            const name = document.createElement('span');
            name.textContent = z.name;

            item.appendChild(badge);
            item.appendChild(name);
            item.addEventListener('click', () => selectZone(z.id, item));
            li.appendChild(item);

            const children = childMap[z.id] || [];
            if (children.length > 0) {
                li.appendChild(buildList(children, childMap));
            }
            ul.appendChild(li);
        });
        return ul;
    }

    function selectZone(id, el) {
        document.querySelectorAll('.sc-zone-item.selected')
                .forEach(e => e.classList.remove('selected'));
        el.classList.add('selected');
        selectedZoneId = id;
        const input = document.getElementById('selectedZoneId');
        if (input) input.value = id;
        const label = document.getElementById('selectedZoneLabel');
        if (label) label.textContent = 'Zone #' + id + ' selected';
    }

    return { render, getSelectedZoneId: () => selectedZoneId };
})();

/* =========================================================
   3. Street Light Toggle
   ========================================================= */
function toggleLight(lightId, currentState, ctxPath) {
    const newState = currentState ? 'off' : 'on';
    const card = document.getElementById('light-' + lightId);
    if (card) {
        card.style.opacity = '0.6';
        card.style.pointerEvents = 'none';
    }

    scPost(ctxPath + '/app/street-lights', {
        id: lightId,
        action: newState
    }).then(resp => {
        if (resp.ok || resp.redirected) {
            window.location.reload();
        } else {
            alert('Failed to toggle light. Please try again.');
            if (card) { card.style.opacity = '1'; card.style.pointerEvents = ''; }
        }
    }).catch(err => {
        console.error(err);
        if (card) { card.style.opacity = '1'; card.style.pointerEvents = ''; }
    });
}

/* =========================================================
   4. Policy Expression Validator (Interpreter pattern demo)
   ========================================================= */
const PolicyValidator = (() => {
    function validate(expression) {
        if (!expression || !expression.trim()) {
            return { valid: false, message: 'Expression cannot be empty.' };
        }
        // Client-side basic validation — full validation is done server-side
        // via the Interpreter pattern (PolicyParser.java)
        const tokens = expression.trim().split(/\s+/);
        if (tokens.length < 3) {
            return { valid: false, message: 'Expression must contain at least: VARIABLE OPERATOR NUMBER' };
        }
        const operators = ['<', '>', '<=', '>=', '==', '!='];
        let hasOperator = operators.some(op => expression.includes(op));
        if (!hasOperator) {
            return { valid: false, message: 'Expression must contain a comparison operator (<, >, <=, >=, ==, !=)' };
        }
        return { valid: true, message: 'Syntax looks valid. Full validation via Interpreter pattern on submit.' };
    }

    function bindValidator(inputId, feedbackId) {
        const input = document.getElementById(inputId);
        const feedback = document.getElementById(feedbackId);
        if (!input || !feedback) return;

        input.addEventListener('input', () => {
            const result = validate(input.value);
            feedback.textContent = result.message;
            feedback.className = result.valid ? 'form-text text-success' : 'form-text text-warning';
        });
    }

    return { validate, bindValidator };
})();

/* =========================================================
   5. Energy Chart (Chart.js)
   ========================================================= */
function renderEnergyChart(canvasId, labels, kwhData) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;
    if (typeof Chart === 'undefined') return; // Chart.js not loaded

    new Chart(canvas, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'kWh Reading',
                data: kwhData,
                borderColor: '#0b3d5c',
                backgroundColor: 'rgba(11, 61, 92, 0.1)',
                tension: 0.4,
                fill: true,
                pointBackgroundColor: '#136f63',
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: { mode: 'index', intersect: false }
            },
            scales: {
                y: {
                    beginAtZero: false,
                    title: { display: true, text: 'kWh' }
                },
                x: {
                    title: { display: true, text: 'Reading Time' }
                }
            }
        }
    });
}

/* =========================================================
   6. Auto-dismiss flash alerts
   ========================================================= */
function initFlashDismiss() {
    const alerts = document.querySelectorAll('.alert:not(.alert-danger):not(.alert-danger)');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert.parentNode) {
                alert.style.transition = 'opacity 0.5s';
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 500);
            }
        }, 5000);
    });
}

/* =========================================================
   7. Confirm dangerous actions
   ========================================================= */
function confirmAction(message) {
    return window.confirm(message || 'Are you sure?');
}

/* =========================================================
   8. Init on DOM ready
   ========================================================= */
document.addEventListener('DOMContentLoaded', () => {
    initFlashDismiss();

    // Bind policy validator if on policy page
    PolicyValidator.bindValidator('expression', 'expressionFeedback');

    // Zone tree auto-render if data is embedded
    const zoneDataEl = document.getElementById('zoneTreeData');
    if (zoneDataEl) {
        try {
            const zones = JSON.parse(zoneDataEl.textContent);
            ZoneTree.render(zones, 'zoneTreeContainer');
        } catch (e) {
            console.error('Failed to parse zone tree data:', e);
        }
    }
});
