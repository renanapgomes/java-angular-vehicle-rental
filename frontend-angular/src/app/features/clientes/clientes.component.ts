import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ClientsService } from '../../shared/services/clients.service';
import { Client } from '../../shared/models/client.model';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
})
export class ClientesComponent implements OnInit {
  clients: Client[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(private readonly clientsService: ClientsService) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.errorMessage = null;
    this.loading = true;
    this.clientsService.list().subscribe({
      next: (c) => (this.clients = c),
      error: () => {
        this.errorMessage = 'Erro ao carregar clientes.';
        this.loading = false;
      },
      complete: () => (this.loading = false),
    });
  }

  excluir(c: Client): void {
    this.errorMessage = null;
    const ok = window.confirm(`Excluir cliente ${c.fullName}?`);
    if (!ok) return;

    this.loading = true;
    this.clientsService.delete(c.id).subscribe({
      next: () => {
        this.clients = this.clients.filter((x) => x.id !== c.id);
      },
      error: (e: HttpErrorResponse) => {
        this.errorMessage = e?.error?.message || 'Não foi possível excluir o cliente.';
      },
      complete: () => (this.loading = false),
    });
  }
}

