/**
 * SmartCity360 – Sign-up page JS
 * - Password strength meter
 * - Confirm password match check
 * - Toggle password visibility
 */
'use strict';

document.addEventListener('DOMContentLoaded', () => {

  /* ── Password strength ─────────────────────────────────── */
  const pwdInput  = document.getElementById('password');
  const strengthFill = document.getElementById('strengthFill');
  const strengthLabel = document.getElementById('strengthLabel');

  const levels = [
    { label: 'Too short',  color: '#dc2626', w: '20%' },
    { label: 'Weak',       color: '#f59e0b', w: '40%' },
    { label: 'Fair',       color: '#eab308', w: '60%' },
    { label: 'Good',       color: '#22c55e', w: '80%' },
    { label: 'Strong',     color: '#16a34a', w: '100%' },
  ];

  function checkStrength(pwd) {
    if (!strengthFill) return;
    let score = 0;
    if (pwd.length >= 8)           score++;
    if (pwd.length >= 12)          score++;
    if (/[A-Z]/.test(pwd))         score++;
    if (/[0-9]/.test(pwd))         score++;
    if (/[^A-Za-z0-9]/.test(pwd))  score++;
    const idx = Math.min(score, 4);
    strengthFill.style.width      = score === 0 ? '0' : levels[idx].w;
    strengthFill.style.background = levels[idx].color;
    if (strengthLabel) strengthLabel.textContent = score === 0 ? '' : levels[idx].label;
  }

  if (pwdInput) pwdInput.addEventListener('input', () => checkStrength(pwdInput.value));

  /* ── Confirm password ─────────────────────────────────────── */
  const confirmInput = document.getElementById('confirmPassword');
  const matchFeedback = document.getElementById('matchFeedback');

  if (confirmInput) {
    confirmInput.addEventListener('input', function () {
      const pwd = pwdInput ? pwdInput.value : '';
      if (!matchFeedback) return;
      if (!this.value) {
        matchFeedback.textContent = '';
        matchFeedback.className = 'sc-form-hint';
        return;
      }
      if (this.value === pwd) {
        matchFeedback.textContent = '✓ Passwords match';
        matchFeedback.className = 'sc-form-hint text-green-600';
      } else {
        matchFeedback.textContent = 'Passwords do not match';
        matchFeedback.className = 'sc-form-hint text-red-600';
      }
    });
  }

  /* ── Toggle visibility ──────────────────────────────────── */
  document.querySelectorAll('[data-toggle-pwd]').forEach(btn => {
    const targetId = btn.dataset.togglePwd;
    const target = document.getElementById(targetId);
    if (!target) return;
    btn.addEventListener('click', () => {
      target.type = target.type === 'password' ? 'text' : 'password';
    });
  });
});
