/**
 * SmartCity360 – Toast Notification System
 * Usage: Toast.show('Message text', 'success' | 'error' | 'warning' | 'info')
 */
'use strict';

const Toast = (() => {
  const icons = {
    success: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>`,
    error:   `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>`,
    warning: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>`,
    info:    `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>`
  };

  function getHost() {
    let host = document.getElementById('toast-host');
    if (!host) {
      host = document.createElement('div');
      host.id = 'toast-host';
      document.body.appendChild(host);
    }
    return host;
  }

  function show(message, type = 'info', duration = 4000) {
    const host = getHost();
    const cls = `sc-toast sc-toast-${type}`;
    const el = document.createElement('div');
    el.className = cls;
    el.setAttribute('role', 'alert');
    el.innerHTML = `
      <span class="flex-shrink-0">${icons[type] || icons.info}</span>
      <span class="flex-1">${escHtml(message)}</span>
      <button class="sc-toast-close" aria-label="Close">&times;</button>
    `;

    el.querySelector('.sc-toast-close').addEventListener('click', () => dismiss(el));
    host.appendChild(el);

    // Auto-dismiss
    const timer = setTimeout(() => dismiss(el), duration);
    el._timer = timer;

    return el;
  }

  function dismiss(el) {
    clearTimeout(el._timer);
    el.classList.add('hide');
    setTimeout(() => el.remove(), 350);
  }

  function escHtml(str) {
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  return { show };
})();
