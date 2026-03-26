import { Component, OnInit } from '@angular/core';
import { ClientsService } from '../../shared/services/clients.service';
import { VehiclesService } from '../../shared/services/vehicles.service';
import { RentalsService } from '../../shared/services/rentals.service';
import { Client } from '../../shared/models/client.model';
import { Rental } from '../../shared/models/rental.model';
import { Vehicle } from '../../shared/models/vehicle.model';
import { countInclusiveDaysIso } from '../../shared/utils/date-utils';

@Component({
  selector: 'app-alugueis',
  templateUrl: './alugueis.component.html',
})
export class AlugueisComponent implements OnInit {
  clients: Client[] = [];
  vehicles: Vehicle[] = [];
  rentals: Rental[] = [];

  selectedClientId: string = '';
  selectedVehicleId: string = '';
  fromDate: string = '';
  toDate: string = '';

  availableVehicles: Vehicle[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private readonly clientsService: ClientsService,
    private readonly vehiclesService: VehiclesService,
    private readonly rentalsService: RentalsService
  ) {}

  ngOnInit(): void {
    this.loadClients();
    this.loadVehicles();
    this.loadRentals();
  }

  loadClients(): void {
    this.clientsService.list().subscribe({
      next: (c) => (this.clients = c),
      error: () => (this.errorMessage = 'Erro ao carregar clientes.'),
    });
  }

  loadVehicles(): void {
    this.vehiclesService.list().subscribe({
      next: (v) => (this.vehicles = v),
      error: () => (this.errorMessage = 'Erro ao carregar veículos.'),
    });
  }

  loadRentals(): void {
    this.rentalsService.list().subscribe({
      next: (r) => (this.rentals = r),
      error: () => (this.errorMessage = 'Erro ao carregar histórico de aluguéis.'),
    });
  }

  carregarDisponiveis(): void {
    this.errorMessage = null;
    if (!this.fromDate || !this.toDate) {
      this.errorMessage = 'Informe data inicial e final.';
      return;
    }

    const from = new Date(this.fromDate);
    const to = new Date(this.toDate);
    if (Number.isNaN(from.getTime()) || Number.isNaN(to.getTime())) {
      this.errorMessage = 'Datas inválidas.';
      return;
    }
    if (to < from) {
      this.errorMessage = 'A data final não pode ser anterior à data inicial.';
      return;
    }

    this.loading = true;
    this.vehiclesService.listAvailable(this.fromDate, this.toDate).subscribe({
      next: (v) => {
        this.availableVehicles = v;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Erro ao consultar disponibilidade.';
      },
    });
  }

  get previewTotalAmount(): number | null {
    const v = this.vehicles.find((x) => x.id === this.selectedVehicleId) ?? null;
    if (!v || !this.fromDate || !this.toDate) return null;
    const days = countInclusiveDaysIso(this.fromDate, this.toDate);
    if (days < 1) return null;
    return Math.round(v.dailyRate * days * 100) / 100;
  }

  formatMoney(value: number): string {
    // Formatação no padrão pt-BR com símbolo monetário (R$).
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(value);
  }

  getVehicleLabelByRental(r: Rental): string {
    const vehicle = this.vehicles.find((v) => v.id === r.vehicleId) ?? null;
    if (vehicle) {
      return `${vehicle.plate} - ${vehicle.brand} ${vehicle.model}`.trim();
    }
    return `${r.vehiclePlate || r.vehicleId}`;
  }

  getRentalStatusLabel(status: Rental['status']): string {
    return status === 'ACTIVE' ? 'Ativo' : 'Cancelado';
  }

  registrar(): void {
    this.errorMessage = null;
    if (!this.selectedClientId || !this.selectedVehicleId || !this.fromDate || !this.toDate) {
      this.errorMessage = 'Selecione cliente, veículo e período.';
      return;
    }

    const from = new Date(this.fromDate);
    const to = new Date(this.toDate);
    if (Number.isNaN(from.getTime()) || Number.isNaN(to.getTime())) {
      this.errorMessage = 'Datas inválidas.';
      return;
    }
    if (to < from) {
      this.errorMessage = 'A data final não pode ser anterior à data inicial.';
      return;
    }

    this.loading = true;
    this.rentalsService
      .create({
        vehicleId: this.selectedVehicleId,
        clientId: this.selectedClientId,
        startDate: this.fromDate,
        endDate: this.toDate,
      })
      .subscribe({
        next: async () => {
          this.loading = false;
          this.loadRentals();
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Falha ao registrar aluguel (verifique disponibilidade).';
        },
      });
  }

  cancelar(r: Rental): void {
    this.errorMessage = null;
    this.loading = true;
    this.rentalsService.cancel(r.id).subscribe({
      next: async () => {
        this.loading = false;
        this.loadRentals();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Falha ao cancelar aluguel.';
      },
    });
  }
}

