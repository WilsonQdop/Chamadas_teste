
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BackupService {
  
  private readonly API_URL = 'https://localhost:8443/backup';

  constructor(private http: HttpClient) { }

  executarBackup(): Observable<string> {
    
    return this.http.post(`${this.API_URL}/execute`, {}, { responseType: 'text' });
  }

  restaurarBackup(fileName: string): Observable<string> {
    return this.http.post(`${this.API_URL}/restore?fileName=${fileName}`, {}, { responseType: 'text' });
  }

  listarArquivos(): Observable<string[]> {
  return this.http.get<string[]>(`${this.API_URL}/files`);
}
}
