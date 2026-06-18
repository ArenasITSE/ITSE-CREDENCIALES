import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn:'root'
})
export class AuditoriaService {

  private http = inject(HttpClient);

  listar(){

    return this.http.get(
      `${environment.apiUrl}/auditoria`
    );

  }

}