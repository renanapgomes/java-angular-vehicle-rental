import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Vehicle } from '../models/vehicle.model';

@Injectable({ providedIn: 'root' })
export class VehiclesService {
  private readonly base = `${environment.apiUrl}/vehicles`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.base);
  }

  listAvailable(from: string, to: string): Observable<Vehicle[]> {
    const params = new HttpParams().set('from', from).set('to', to);
    return this.http.get<Vehicle[]>(`${this.base}/available`, { params });
  }

  create(payload: {
    plate: string;
    brand: string;
    model: string;
    year?: number | null;
    dailyRate: number;
  }): Observable<Vehicle> {
    return this.http.post<Vehicle>(this.base, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}

