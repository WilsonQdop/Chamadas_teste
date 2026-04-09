import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface CalledRequest {
  title: string;
  description: string;
  category: 'HIGH' | 'MEDIA' | 'LOW';
}

@Injectable({
  providedIn: 'root'
})
export class CalledService {
  private readonly API_URL = 'https://localhost:8443/called';

  constructor(private http: HttpClient) {}

  create(called: CalledRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/create`, called);
  }
}