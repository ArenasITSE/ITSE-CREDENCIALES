import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { Perfil } from '../models/perfil';

@Injectable({
  providedIn:'root'
})
export class PerfilService {

  private http = inject(HttpClient);

  perfil(){

    return this.http.get<Perfil>(
      `${environment.apiUrl}/perfil`
    );

  }

  cambiarPassword(data:any){

    return this.http.put(
      `${environment.apiUrl}/perfil/cambiar-password`,
      data
    );

  }

}