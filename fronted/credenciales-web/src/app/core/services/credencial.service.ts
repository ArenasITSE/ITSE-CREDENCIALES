import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CredencialService {

  private http = inject(HttpClient);

  //==========================================
  // LISTAR
  //==========================================

  listar() {

    return this.http.get(

      `${environment.apiUrl}/credenciales`

    );

  }

  //==========================================
  // OBTENER
  //==========================================

  obtener(id:number){

    return this.http.get(

      `${environment.apiUrl}/credenciales/${id}`

    );

  }

  //==========================================
  // DASHBOARD
  //==========================================

  dashboard(){

    return this.http.get(

      `${environment.apiUrl}/credenciales/dashboard`

    );

  }

  //==========================================
  // BUSCAR
  //==========================================

  buscar(texto:string){

    return this.http.get(

      `${environment.apiUrl}/credenciales/buscar?texto=${texto}`

    );

  }

  //==========================================
  // GENERAR
  //==========================================

  generar(id:number){

    return this.http.post(

      `${environment.apiUrl}/credenciales/generar/${id}`,

      {}

    );

  }

  //==========================================
  // REGENERAR
  //==========================================

  regenerar(id:number){

    return this.http.post(

      `${environment.apiUrl}/credenciales/${id}/regenerar`,

      {}

    );

  }

  //==========================================
  // VALIDAR
  //==========================================

  validar(id:number){

    return this.http.put(

      `${environment.apiUrl}/credenciales/${id}/validar`,

      {}

    );

  }

  //==========================================
  // CANCELAR
  //==========================================

  cancelar(id:number){

    return this.http.put(

      `${environment.apiUrl}/credenciales/${id}/cancelar`,

      {}

    );

  }

  //==========================================
  // ACTIVAR
  //==========================================

  activar(id:number){

    return this.http.put(

      `${environment.apiUrl}/credenciales/${id}/activar`,

      {}

    );

  }

  //==========================================
  // ELIMINAR
  //==========================================

  eliminar(id:number){

    return this.http.delete(

      `${environment.apiUrl}/credenciales/${id}`

    );

  }

  //==========================================
  // HISTORIAL
  //==========================================

  historial(id:number){

    return this.http.get(

      `${environment.apiUrl}/credenciales/${id}/historial`

    );

  }

  //==========================================
  // REGENERAR PDF
  //==========================================

  regenerarPdf(id:number){

    return this.http.post(

      `${environment.apiUrl}/credenciales/pdf/${id}/regenerar`,

      {}

    );

  }

  //==========================================
  // DESCARGAR PDF
  //==========================================

  descargarPdf(id:number){

    window.open(

      `${environment.apiUrl}/credenciales/pdf/${id}/download`,

      "_blank"

    );

  }

  //==========================================
  // VER PDF
  //==========================================

  verPdf(id:number){

    window.open(

      `${environment.apiUrl}/credenciales/pdf/${id}`,

      "_blank"

    );

  }

}