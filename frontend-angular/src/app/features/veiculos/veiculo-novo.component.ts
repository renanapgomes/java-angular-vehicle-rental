import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { VehiclesService } from '../../shared/services/vehicles.service';

@Component({
  selector: 'app-veiculo-novo',
  templateUrl: './veiculo-novo.component.html',
})
export class VeiculoNovoComponent {
  plate = '';
  brand = '';
  model = '';
  year: number | null = null;
  dailyRate: number | null = null;

  loading = false;
  errorMessage: string | null = null;

  constructor(
    private readonly router: Router,
    private readonly vehiclesService: VehiclesService
  ) {}

  back(): void {
    this.router.navigate(['/veiculos']);
  }

  save(): void {
    this.errorMessage = null;

    const plateTrim = this.plate.trim();
    const brandTrim = this.brand.trim();
    const modelTrim = this.model.trim();

    const normalizedPlate = plateTrim.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();

    if (!plateTrim) {
      this.errorMessage = 'Placa precisa ser preenchida.';
      return;
    }

    if (normalizedPlate.length < 7) {
      this.errorMessage = 'Placa inválida (mínimo 7 caracteres alfanuméricos).';
      return;
    }

    if (!brandTrim) {
      this.errorMessage = 'Marca precisa ser preenchida.';
      return;
    }

    if (!modelTrim) {
      this.errorMessage = 'Modelo precisa ser preenchido.';
      return;
    }

    if (this.dailyRate == null || Number.isNaN(this.dailyRate) || this.dailyRate <= 0) {
      this.errorMessage = 'Diária precisa ser maior que zero.';
      return;
    }

    if (this.year != null && !Number.isNaN(this.year) && (this.year < 1900 || this.year > 2100)) {
      this.errorMessage = 'Ano do veículo inválido.';
      return;
    }

    this.loading = true;
    this.vehiclesService
      .create({
        plate: plateTrim,
        brand: brandTrim,
        model: modelTrim,
        year: this.year,
        dailyRate: this.dailyRate,
      })
      .subscribe({
        next: async () => {
          this.loading = false;
          this.back();
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Falha ao cadastrar veículo.';
        },
      });
  }
}

