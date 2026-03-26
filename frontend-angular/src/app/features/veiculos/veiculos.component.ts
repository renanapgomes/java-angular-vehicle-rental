import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { VehiclesService } from '../../shared/services/vehicles.service';
import { Vehicle } from '../../shared/models/vehicle.model';

@Component({
  selector: 'app-veiculos',
  templateUrl: './veiculos.component.html',
})
export class VeiculosComponent implements OnInit {
  vehicles: Vehicle[] = [];
  available: Vehicle[] = [];

  fromDate = '';
  toDate = '';

  loading = false;
  errorMessage: string | null = null;

  constructor(private readonly vehiclesService: VehiclesService) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.errorMessage = null;
    this.loading = true;
    this.vehiclesService.list().subscribe({
      next: (v) => (this.vehicles = v),
      error: (e) => {
        this.errorMessage = 'Erro ao carregar veículos.';
      },
      complete: () => (this.loading = false),
    });
  }

  consultarDisponibilidade(): void {
    this.errorMessage = null;
    if (!this.fromDate || !this.toDate) {
      this.errorMessage = 'Informe data inicial e final.';
      return;
    }
    this.loading = true;
    this.vehiclesService.listAvailable(this.fromDate, this.toDate).subscribe({
      next: (v) => (this.available = v),
      error: () => {
        this.errorMessage = 'Erro ao consultar disponibilidade.';
      },
      complete: () => (this.loading = false),
    });
  }

  formatMoney(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  excluir(v: Vehicle): void {
    this.errorMessage = null;
    const ok = window.confirm(`Excluir veículo ${v.plate} - ${v.brand} ${v.model}?`);
    if (!ok) return;

    this.loading = true;
    this.vehiclesService.delete(v.id).subscribe({
      next: () => {
        this.vehicles = this.vehicles.filter((x) => x.id !== v.id);
        this.available = this.available.filter((x) => x.id !== v.id);
      },
      error: (e: HttpErrorResponse) => {
        this.errorMessage = e?.error?.message || 'Não foi possível excluir o veículo.';
      },
      complete: () => (this.loading = false),
    });
  }
}

