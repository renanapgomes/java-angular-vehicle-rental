import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VeiculosComponent } from './features/veiculos/veiculos.component';
import { VeiculoNovoComponent } from './features/veiculos/veiculo-novo.component';
import { ClientesComponent } from './features/clientes/clientes.component';
import { ClienteNovoComponent } from './features/clientes/cliente-novo.component';
import { AlugueisComponent } from './features/alugueis/alugueis.component';

const routes: Routes = [
  { path: '', redirectTo: 'veiculos', pathMatch: 'full' },
  { path: 'veiculos', component: VeiculosComponent },
  { path: 'veiculos/novo', component: VeiculoNovoComponent },
  { path: 'clientes', component: ClientesComponent },
  { path: 'clientes/novo', component: ClienteNovoComponent },
  { path: 'alugueis', component: AlugueisComponent },
  { path: '**', redirectTo: 'veiculos' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
