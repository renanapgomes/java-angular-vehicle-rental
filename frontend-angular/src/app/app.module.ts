import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { VeiculosComponent } from './features/veiculos/veiculos.component';
import { VeiculoNovoComponent } from './features/veiculos/veiculo-novo.component';
import { ClientesComponent } from './features/clientes/clientes.component';
import { ClienteNovoComponent } from './features/clientes/cliente-novo.component';
import { AlugueisComponent } from './features/alugueis/alugueis.component';
import { CpfMaskDirective } from './shared/directives/cpf-mask.directive';
import { PhoneMaskDirective } from './shared/directives/phone-mask.directive';

@NgModule({
  declarations: [
    AppComponent,
    VeiculosComponent,
    VeiculoNovoComponent,
    ClientesComponent,
    ClienteNovoComponent,
    AlugueisComponent,
    CpfMaskDirective,
    PhoneMaskDirective
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
