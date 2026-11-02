/**
 * SmartCity360 – Complaint detail page JS
 * - Confirm before submitting escalate / unresolved actions
 */
'use strict';

document.addEventListener('DOMContentLoaded', () => {
  const actionForm = document.getElementById('complaintActionForm');
  if (!actionForm) return;

  const messages = {
    escalate:   'Escalate this complaint to the next level?',
    unresolved: 'Mark this complaint as unresolved?'
  };

  actionForm.querySelectorAll('button[name="action"]').forEach(btn => {
    btn.addEventListener('click', function (e) {
      const msg = messages[this.value];
      if (msg && !confirm(msg)) e.preventDefault();
    });
  });
});