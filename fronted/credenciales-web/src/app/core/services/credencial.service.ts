import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn:'root'
})
export class CredencialService {

  private http = inject(HttpClient);

  listar(){

    return this.http.get(
      `${environment.apiUrl}/credenciales`
    );

  }

  generar(id:number){

    return this.http.post(
      `${environment.apiUrl}/credenciales/generar/${id}`,
      {}
    );

  }

  cancelar(id:number){

    return this.http.put(
      `${environment.apiUrl}/credenciales/${id}/cancelar`,
      {}
    );

  }

  activar(id:number){

    return this.http.put(
      `${environment.apiUrl}/credenciales/${id}/activar`,
      {}
    );

  }

  regenerarPdf(id:number){

    return this.http.post(
      `${environment.apiUrl}/credenciales/pdf/${id}/regenerar`,
      {}
    );

  }

  descargarPdf(id:number){

    window.open(
      `${environment.apiUrl}/credenciales/pdf/${id}/download`,
      "_blank"
    );

  }

}