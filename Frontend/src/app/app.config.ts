import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http'; // Adicione withInterceptors aqui
import { provideToastr } from 'ngx-toastr';
import { authInterceptor } from '../../src/app/intercepto/auth.interceptor'; // Importe seu interceptor

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideToastr(),
    // Unificamos o Fetch e os Interceptors em uma única chamada:
    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptor])
    )
  ]
};