/**
 * SmartCity360 – Policy page JS
 * - Expression example buttons
 * - Client-side expression syntax preview
 */
'use strict';

document.addEventListener('DOMContentLoaded', () => {
  const exprInput = document.getElementById('expression');
  const feedback  = document.getElementById('expressionFeedback');

  /* ── Example expression buttons ─────────────────────────── */
  document.querySelectorAll('[data-expr]').forEach(btn => {
    btn.addEventListener('click', () => {
      if (exprInput) {
        exprInput.value = btn.dataset.expr;
        exprInput.dispatchEvent(new Event('input'));
        exprInput.focus();
      }
    });
  });

  /* ── Basic client-side validation preview ───────────────── */
  function validate(expr) {
    if (!expr || !expr.trim()) return { valid: false, msg: 'Expression cannot be empty.' };
    const tokens = expr.trim().split(/\s+/);
    if (tokens.length < 3) return { valid: false, msg: 'Expression must have at least: field operator value.' };
    const ops = ['>', '<', '>=', '<=', '==', '!='];
    if (!ops.includes(tokens[1]) && tokens[1].toUpperCase() !== 'AND' && tokens[1].toUpperCase() !== 'OR') {
      return { valid: false, msg: `Unknown operator "${tokens[1]}". Expected: ${ops.join(', ')}, AND, OR.` };
    }
    return { valid: true, msg: '✓ Expression looks valid (full check done server-side).' };
  }

  if (exprInput && feedback) {
    exprInput.addEventListener('input', () => {
      const result = validate(exprInput.value);
      feedback.textContent = result.msg;
      feedback.className = result.valid ? 'sc-form-hint text-green-600 mt-1' : 'sc-form-hint text-amber-600 mt-1';
    });
  }

  /* ── Delete confirmation ─────────────────────────────────── */
  document.querySelectorAll('[data-confirm]').forEach(el => {
    el.addEventListener('click', function (e) {
      if (!confirm(this.dataset.confirm)) e.preventDefault();
    });
  });
});
