import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

// Defina a interface para bater com o LoginResponseDTO do seu Java
export interface LoginResponse {
  token: string;
  expiresIn: number;
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  // Ajuste para a URL do seu backend (exemplo com HTTPS que configuramos)
  private readonly API_URL = 'https://localhost:8443/token';

  constructor(private httpClient: HttpClient) { }

  login(email: string, password: string) {
    // O seu LoginRequestDTO no Java espera 'email' e 'senha'
    return this.httpClient.post<LoginResponse>(`${this.API_URL}/login`, { email, password })
    .pipe(
     // No tap do login:
    tap((value: any) => {
      if (value && value.accessToken) {
      sessionStorage.setItem('auth-token', value.accessToken);
  }
  })
        );
  }
}