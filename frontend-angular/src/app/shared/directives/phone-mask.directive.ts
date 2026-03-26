import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appPhoneMask]',
})
export class PhoneMaskDirective {
  constructor(private readonly el: ElementRef<HTMLInputElement>) {}

  @HostListener('input', ['$event'])
  onInput(): void {
    const input = this.el.nativeElement;
    const formatted = this.formatPhone(input.value);
    if (input.value !== formatted) {
      input.value = formatted;
      // Garante que o ngModel grave o valor já formatado.
      input.dispatchEvent(new Event('input', { bubbles: true }));
    }
  }

  private formatPhone(value: string): string {
    const digits = (value || '').replace(/\D/g, '').slice(0, 11);
    if (!digits.length) return '';

    const area = digits.substring(0, 2);
    const rest = digits.substring(2);

    let out = `(${area})`;

    if (!rest.length) return out + ' ';

    if (rest.length <= 4) return out + ' ' + rest;

    if (rest.length <= 8) {
      return out + ' ' + rest.substring(0, 4) + '-' + rest.substring(4);
    }

    // Mobile (9 dígitos)
    return out + ' ' + rest.substring(0, 5) + '-' + rest.substring(5, 9);
  }
}

