import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Carrera } from '../models/carrera';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn:'root'
})
export class CarreraService {

  private http = inject(HttpClient);

  listar():Observable<Carrera[]>{

    return this.http.get<Carrera[]>(
      `${environment.apiUrl}/carreras`
    );

  }

}