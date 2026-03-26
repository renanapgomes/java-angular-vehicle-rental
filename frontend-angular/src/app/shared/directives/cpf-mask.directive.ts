import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appCpfMask]',
})
export class CpfMaskDirective {
  constructor(private readonly el: ElementRef<HTMLInputElement>) {}

  @HostListener('input', ['$event'])
  onInput(): void {
    const input = this.el.nativeElement;
    const formatted = this.formatCpf(input.value);
    if (input.value !== formatted) {
      input.value = formatted;
      // Garante que o ngModel grave o valor já formatado (evita "voltar" para não-formatado).
      input.dispatchEvent(new Event('input', { bubbles: true }));
    }
  }

  private formatCpf(value: string): string {
    const digits = (value || '').replace(/\D/g, '').slice(0, 11);
    const len = digits.length;
    if (len <= 3) return digits;

    let out = digits.substring(0, 3);
    if (len > 3) out += '.' + digits.substring(3, Math.min(6, len));
    if (len > 6) out += '.' + digits.substring(6, Math.min(9, len));
    if (len > 9) out += '-' + digits.substring(9, Math.min(11, len));
    return out;
  }
}

