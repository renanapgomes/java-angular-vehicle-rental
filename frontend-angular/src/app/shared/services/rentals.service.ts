import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Rental } from '../models/rental.model';

@Injectable({ providedIn: 'root' })
export class RentalsService {
  private readonly base = `${environment.apiUrl}/rentals`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<Rental[]> {
    return this.http.get<Rental[]>(this.base);
  }

  create(payload: { vehicleId: string; clientId: string; startDate: string; endDate: string }): Observable<Rental> {
    return this.http.post<Rental>(this.base, payload);
  }

  cancel(id: string): Observable<Rental> {
    return this.http.post<Rental>(`${this.base}/${id}/cancel`, {});
  }
}

