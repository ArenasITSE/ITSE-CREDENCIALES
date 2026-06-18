import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { StorageService } from '../../core/services/storage.service';

@Component({
  selector: 'app-alumnos',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './alumnos.html',
  styleUrl: './alumnos.css'
})
export class Alumnos implements OnInit {

  private http = inject(HttpClient);
  private storage = inject(StorageService);

  cargando = true;

  alumno: any = {};

  iniciales = '';

  nombreCorto = '';

  fechaActual = new Date();

  ngOnInit(): void {

    this.obtenerPerfil();

  }

  //===========================================
  // OBTENER PERFIL DEL ALUMNO
  //===========================================

  obtenerPerfil(): void {

    this.http.get<any>(
      `${environment.apiUrl}/alumno/inicio`,
      {
        headers: {
          Authorization: this.storage.obtenerAuthorization()
        }
      }
    ).subscribe({

      next: (resp) => {

        this.alumno = resp;

        this.generarIniciales();

        this.cargando = false;

      },

      error: (err) => {

        console.error(err);

        this.cargando = false;

      }

    });

  }

  //===========================================
  // GENERAR INICIALES
  //===========================================

  generarIniciales(): void {

    if (!this.alumno.nombreCompleto) {

      this.iniciales = '';

      this.nombreCorto = '';

      return;

    }

    const partes = this.alumno.nombreCompleto
      .trim()
      .split(' ');

    if (partes.length === 1) {

      this.iniciales =
        partes[0].charAt(0).toUpperCase();

      this.nombreCorto =
        partes[0];

      return;

    }

    this.iniciales =

      partes[0].charAt(0).toUpperCase() +

      partes[1].charAt(0).toUpperCase();

    this.nombreCorto =

      partes[0] + ' ' + partes[1];

  }

  //===========================================
  // SABER SI EXISTE CREDENCIAL
  //===========================================

  get tieneCredencial(): boolean {

    return this.alumno?.credencialId != null;

  }

  //===========================================
  // DESCARGAR PDF
  //===========================================

  descargarCredencial(): void {

    if (!this.tieneCredencial) {

      return;

    }

    this.http.get(

      `${environment.apiUrl}/credenciales/pdf/${this.alumno.credencialId}/download`,

      {

        headers: {

          Authorization: this.storage.obtenerAuthorization()

        },

        responseType: 'blob'

      }

    ).subscribe({

      next: (archivo: Blob) => {

        const url = window.URL.createObjectURL(archivo);

        const a = document.createElement('a');

        a.href = url;

        a.download = `Credencial-${this.alumno.matricula}.pdf`;

        a.click();

        window.URL.revokeObjectURL(url);

      },

      error: (err) => {

        console.error(err);

      }

    });

  }

  //===========================================
  // ABRIR PDF
  //===========================================

  abrirCredencial(): void {

    if (!this.tieneCredencial) {

      return;

    }

    this.http.get(

      `${environment.apiUrl}/credenciales/pdf/${this.alumno.credencialId}`,

      {

        responseType: 'blob',

        headers: {

          Authorization: this.storage.obtenerAuthorization()

        }

      }

    ).subscribe({

      next: (blob: Blob) => {

        const file = new Blob(

          [blob],

          {

            type: 'application/pdf'

          }

        );

        const url = URL.createObjectURL(file);

        window.open(url, '_blank');

      },

      error: (err) => {

        console.error(err);

      }

    });

  }

  //===========================================
  // CERRAR SESIÓN
  //===========================================

  cerrarSesion(): void {

    this.storage.logout();

    location.href = '/login';

  }

}