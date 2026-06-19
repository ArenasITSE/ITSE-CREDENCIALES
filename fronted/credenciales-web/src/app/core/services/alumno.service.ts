import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Alumno } from '../models/alumno';

@Injectable({
  providedIn: 'root'
})
export class AlumnoService {

  private http = inject(HttpClient);

  //==========================================
  // LISTAR
  //==========================================

  listar(): Observable<Alumno[]> {

    return this.http.get<Alumno[]>(

      `${environment.apiUrl}/alumnos`

    );

  }

  //==========================================
  // OBTENER
  //==========================================

  obtener(id:number):Observable<Alumno>{

    return this.http.get<Alumno>(

      `${environment.apiUrl}/alumnos/${id}`

    );

  }

  //==========================================
  // REGISTRAR
  //==========================================

  registrar(data:any){

    return this.http.post(

      `${environment.apiUrl}/alumnos`,

      data

    );

  }

  //==========================================
  // REGISTRAR COMPLETO
  //==========================================

  registrarCompleto(formData:FormData){

    return this.http.post(

      `${environment.apiUrl}/alumnos/registrar-completo`,

      formData

    );

  }

  //==========================================
  // ACTUALIZAR
  //==========================================

  actualizar(id:number,data:any){

    return this.http.put(

      `${environment.apiUrl}/alumnos/${id}`,

      data

    );

  }

  //==========================================
  // CAMBIAR FOTO
  //==========================================

  cambiarFoto(
    id:number,
    foto:File
){

    const formData = new FormData();

    formData.append('foto', foto);

    return this.http.put(

        `${environment.apiUrl}/alumnos/${id}/foto`,

        formData,

        {

            responseType: 'text'

        }

    );

}
  //==========================================
  // RESTABLECER PASSWORD
  //==========================================

  restablecerPassword(id:number){

  return this.http.put(

      `${environment.apiUrl}/alumnos/${id}/restablecer-password`,

      {},

      {

        responseType:'text'

      }

  );

}
  //==========================================
  // ELIMINAR ALUMNO
  //==========================================

  eliminar(id:number){

    return this.http.delete(

      `${environment.apiUrl}/alumnos/${id}`

    );

  }

  //==========================================
  // ELIMINAR CREDENCIAL
  //==========================================

  eliminarCredencial(id:number){

    return this.http.delete(

      `${environment.apiUrl}/alumnos/${id}/credencial`

    );

  }

  //==========================================
  // REGENERAR CREDENCIAL
  //==========================================

  regenerarCredencial(id:number){

    return this.http.put(

      `${environment.apiUrl}/alumnos/${id}/credencial/regenerar`,

      {}

    );

  }

  //==========================================
  // CANCELAR CREDENCIAL
  //==========================================

  cancelarCredencial(id:number){

    return this.http.put(

      `${environment.apiUrl}/alumnos/${id}/credencial/cancelar`,

      {}

    );

  }

  //==========================================
  // ACTIVAR CREDENCIAL
  //==========================================

  activarCredencial(id:number){

    return this.http.put(

      `${environment.apiUrl}/alumnos/${id}/credencial/activar`,

      {}

    );

  }

  //==========================================
  // VALIDAR CREDENCIAL
  //==========================================

  validarCredencial(id:number){

    return this.http.put(

      `${environment.apiUrl}/alumnos/${id}/credencial/validar`,

      {}

    );

  }

}