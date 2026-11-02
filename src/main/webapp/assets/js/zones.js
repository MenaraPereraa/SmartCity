/**
 * SmartCity360 – Zone Explorer JS
 * - Renders an interactive zone tree from embedded JSON
 * - Wires zone selection to the command form
 */
'use strict';

/* ── CSRF / POST helper ──────────────────────────────────────── */
function getCsrfToken() {
  const meta = document.querySelector('meta[name="csrf-token"]');
  return meta ? meta.getAttribute('content') : '';
}

/* ── Zone Tree renderer ─────────────────────────────────────── */
const ZoneTree = (() => {
  let selected = null;

  function render(zones, containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;
    if (!zones || zones.length === 0) {
      container.innerHTML = '<p class="text-sm text-slate-400 px-2">No zones configured.</p>';
      return;
    }
    const childMap = {};
    const roots    = [];
    zones.forEach(z => { childMap[z.id] = []; });
    zones.forEach(z => {
      if (z.parentId && childMap[z.parentId] !== undefined) childMap[z.parentId].push(z);
      else roots.push(z);
    });
    container.innerHTML = '';
    container.appendChild(buildList(roots, childMap));
  }

  function buildList(nodes, childMap) {
    const ul = document.createElement('ul');
    ul.className = 'sc-zone-tree';
    nodes.forEach(z => {
      const li   = document.createElement('li');
      const item = document.createElement('div');
      item.className = 'sc-zone-item';
      item.dataset.id   = z.id;
      item.dataset.name = z.name;

      const badge = document.createElement('span');
      badge.className = 'sc-zone-badge ' + (z.level || '').toLowerCase();
      badge.textContent = z.level || '';

      const name = document.createElement('span');
      name.className = 'flex-1';
      name.textContent = z.name;

      item.appendChild(badge);
      item.appendChild(name);
      item.addEventListener('click', e => { e.stopPropagation(); selectZone(z.id, z.name, item); });
      li.appendChild(item);

      const children = childMap[z.id] || [];
      if (children.length > 0) li.appendChild(buildList(children, childMap));
      ul.appendChild(li);
    });
    return ul;
  }

  function selectZone(id, name, el) {
    document.querySelectorAll('.sc-zone-item.selected').forEach(e => e.classList.remove('selected'));
    el.classList.add('selected');
    selected = id;

    const zoneIdInput = document.getElementById('commandZoneId');
    if (zoneIdInput) zoneIdInput.value = id;

    const label = document.getElementById('selectedZoneLabel');
    if (label) { label.textContent = 'Selected: ' + name; label.classList.remove('hidden'); }

    const alert = document.getElementById('commandZoneAlert');
    if (alert) alert.classList.add('hidden');
  }

  return { render, getSelected: () => selected };
})();

/* ── Init ───────────────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
  const dataEl = document.getElementById('zoneTreeData');
  if (dataEl) {
    try {
      ZoneTree.render(JSON.parse(dataEl.textContent), 'zoneTreeContainer');
    } catch (e) {
      console.error('Zone tree parse error:', e);
    }
  }

  /* Command form guard */
  const commandForm = document.getElementById('commandForm');
  if (commandForm) {
    commandForm.addEventListener('submit', function (e) {
      const zoneId = document.getElementById('commandZoneId');
      if (!zoneId || !zoneId.value) {
        e.preventDefault();
        const alert = document.getElementById('commandZoneAlert');
        if (alert) { alert.classList.remove('hidden'); alert.scrollIntoView({ behavior: 'smooth' }); }
        return;
      }
      const cmd = document.getElementById('commandSelect');
      if (cmd && (cmd.value === 'POWER_OFF' || cmd.value === 'POWER_ON')) {
        if (!confirm(`This will ${cmd.value.replace('_', ' ')} all buildings in the selected zone. Continue?`)) {
          e.preventDefault();
        }
      }
    });
  }
});
