/**
 * SmartCity360 – Sidebar & Shell JS
 * - Marks the active nav link based on the current URL
 * - Handles mobile sidebar toggle
 * - Fires flash-message toasts if the server injected them
 */
'use strict';

document.addEventListener('DOMContentLoaded', () => {

  /* ── Active nav link ────────────────────────────────────── */
  const currentPath = window.location.pathname;
  document.querySelectorAll('.sc-nav-link[data-path]').forEach(link => {
    const path = link.dataset.path;
    if (path && currentPath.startsWith(path)) {
      link.classList.add('active');
    }
  });

  /* ── Mobile sidebar toggle ──────────────────────────────── */
  const sidebar  = document.getElementById('sc-sidebar');
  const overlay  = document.getElementById('sc-sidebar-overlay');
  const openBtn  = document.getElementById('sc-menu-open');
  const closeBtn = document.getElementById('sc-menu-close');

  function openSidebar() {
    sidebar && sidebar.classList.add('open');
    overlay && overlay.classList.remove('hidden');
    document.body.style.overflow = 'hidden';
  }
  function closeSidebar() {
    sidebar && sidebar.classList.remove('open');
    overlay && overlay.classList.add('hidden');
    document.body.style.overflow = '';
  }

  openBtn  && openBtn.addEventListener('click', openSidebar);
  closeBtn && closeBtn.addEventListener('click', closeSidebar);
  overlay  && overlay.addEventListener('click', closeSidebar);

  /* ── Flash-message toasts ───────────────────────────────── */
  const flashEl = document.getElementById('sc-flash-data');
  if (flashEl && typeof Toast !== 'undefined') {
    const msg  = flashEl.dataset.message;
    const type = flashEl.dataset.type || 'info';
    if (msg) Toast.show(msg, type);
  }
});
