import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ClientsService } from '../../shared/services/clients.service';

@Component({
  selector: 'app-cliente-novo',
  templateUrl: './cliente-novo.component.html',
})
export class ClienteNovoComponent {
  fullName = '';
  document = '';
  email = '';
  phone: string | null = null;

  loading = false;
  errorMessage: string | null = null;

  constructor(
    private readonly router: Router,
    private readonly clientsService: ClientsService
  ) {}

  back(): void {
    this.router.navigate(['/clientes']);
  }

  save(): void {
    this.errorMessage = null;

    const fullNameTrim = this.fullName.trim();
    const emailTrim = this.email.trim();
    const cpfDigits = (this.document || '').replace(/\D/g, '');

    if (!fullNameTrim) {
      this.errorMessage = 'Nome precisa ser preenchido.';
      return;
    }

    if (!cpfDigits || cpfDigits.length !== 11) {
      this.errorMessage = 'CPF precisa ter 11 dígitos.';
      return;
    }

    if (!emailTrim) {
      this.errorMessage = 'E-mail precisa ser preenchido.';
      return;
    }

    if (!emailTrim.includes('@')) {
      this.errorMessage = 'E-mail inválido: precisa conter "@".';
      return;
    }

    // Mantém o telefone "opcional" como `null` quando o campo estiver vazio.
    const phoneToSave = this.phone && this.phone.trim().length ? this.phone.trim() : null;

    this.loading = true;
    this.clientsService
      .create({
        fullName: fullNameTrim,
        document: cpfDigits,
        email: emailTrim,
        phone: phoneToSave,
      })
      .subscribe({
        next: () => {
          this.loading = false;
          this.back();
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Falha ao cadastrar cliente.';
        },
      });
  }
}

