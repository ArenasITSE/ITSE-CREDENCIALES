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

  listar(): Observable<Alumno[]> {
    return this.http.get<Alumno[]>(
      `${environment.apiUrl}/alumnos`
    );
  }

  obtener(id: number): Observable<Alumno> {
    return this.http.get<Alumno>(
      `${environment.apiUrl}/alumnos/${id}`
    );
  }

  registrar(data: any) {
    return this.http.post(
      `${environment.apiUrl}/alumnos`,
      data
    );
  }

  /**
   * Nuevo endpoint
   * Registra alumno + fotografía + credencial
   * en una sola petición.
   */
  registrarCompleto(formData: FormData) {
    return this.http.post(
      `${environment.apiUrl}/alumnos/registrar-completo`,
      formData
    );
  }

  eliminar(id: number) {
    return this.http.delete(
      `${environment.apiUrl}/alumnos/${id}`
    );
  }

}