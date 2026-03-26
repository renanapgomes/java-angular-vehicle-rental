import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Client } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientsService {
  private readonly base = `${environment.apiUrl}/clients`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<Client[]> {
    return this.http.get<Client[]>(this.base);
  }

  create(payload: { fullName: string; document: string; email: string; phone?: string | null }): Observable<Client> {
    return this.http.post<Client>(this.base, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}

