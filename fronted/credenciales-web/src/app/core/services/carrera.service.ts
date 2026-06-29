import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import { Carrera } from '../models/carrera';
import { DashboardCarrera } from '../models/dashboard-carrera';

@Injectable({

  providedIn:'root'

})

export class CarreraService{

  private http=inject(HttpClient);

  //==========================================
  // LISTAR
  //==========================================

  listar():Observable<Carrera[]>{

    return this.http.get<Carrera[]>(

      `${environment.apiUrl}/carreras`

    );

  }

  //==========================================
  // OBTENER
  //==========================================

  obtener(id:number){

    return this.http.get<Carrera>(

      `${environment.apiUrl}/carreras/${id}`

    );

  }

  //==========================================
  // REGISTRAR
  //==========================================

  registrar(data:any){

    return this.http.post(

      `${environment.apiUrl}/carreras`,

      data

    );

  }

  //==========================================
  // ACTUALIZAR
  //==========================================

  actualizar(id:number,data:any){

    return this.http.put(

      `${environment.apiUrl}/carreras/${id}`,

      data

    );

  }

  //==========================================
  // ELIMINAR
  //==========================================

  eliminar(id:number){

    return this.http.delete(

      `${environment.apiUrl}/carreras/${id}`

    );

  }

  //==========================================
  // DASHBOARD
  //==========================================

  dashboard(){

    return this.http.get<DashboardCarrera>(

      `${environment.apiUrl}/carreras/dashboard`

    );

  }

}