import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import Swal from 'sweetalert2';

import { CredencialService } from '../../core/services/credencial.service';
import { Credencial } from '../../core/models/credencial';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-credenciales',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './credenciales.html',
  styleUrl: './credenciales.css'
})
export class Credenciales implements OnInit {

  private credencialService = inject(CredencialService);
  api = environment.apiUrl.replace('/api', '');

  //==========================================
  // VARIABLES
  //==========================================

  cargando = false;

  credenciales: Credencial[] = [];

  dashboard: any = {

    activas: 0,

    canceladas: 0,

    validadas: 0,

    porVencer: 0

  };

  textoBusqueda = '';

  credencialSeleccionada: any = null;

  historial: any[] = [];

  mostrarHistorial = false;

  //==========================================
  // INIT
  //==========================================

  ngOnInit(): void {

    this.cargarDashboard();

    this.cargarCredenciales();

  }

  //==========================================
  // DASHBOARD
  //==========================================

  cargarDashboard(): void {

    this.credencialService

      .dashboard()

      .subscribe({

        next: (resp: any) => {

          this.dashboard = resp;

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  //==========================================
  // CARGAR CREDENCIALES
  //==========================================

  cargarCredenciales(): void {

    this.cargando = true;

    this.credencialService

      .listar()

      .subscribe({

        next: (resp: any) => {

          this.credenciales = resp;

          this.cargando = false;

        },

        error: (err) => {

          console.error(err);

          this.cargando = false;

        }

      });

  }
    //==========================================
  // BUSCAR
  //==========================================

  buscar(): void {

    if (this.textoBusqueda.trim() === '') {

      this.cargarCredenciales();

      return;

    }

    this.credencialService

      .buscar(this.textoBusqueda)

      .subscribe({

        next: (resp: any) => {

          this.credenciales = resp;

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  //==========================================
  // LIMPIAR BUSCADOR
  //==========================================

  limpiarBusqueda(): void {

    this.textoBusqueda = '';

    this.cargarCredenciales();

  }

  //==========================================
  // VER PDF
  //==========================================

  verPdf(credencial: Credencial): void {

    this.credencialService

      .verPdf(credencial.id);

  }

  //==========================================
  // DESCARGAR PDF
  //==========================================

  descargarPdf(credencial: Credencial): void {

    this.credencialService

      .descargarPdf(credencial.id);

  }

  //==========================================
  // REGENERAR PDF
  //==========================================

  regenerarPdf(credencial: Credencial): void {

    Swal.fire({

      title: '¿Regenerar PDF?',

      text: 'Se volverá a generar el archivo PDF de esta credencial.',

      icon: 'question',

      showCancelButton: true,

      confirmButtonText: 'Sí, regenerar',

      cancelButtonText: 'Cancelar'

    }).then(result => {

      if (!result.isConfirmed) {

        return;

      }

      this.credencialService

        .regenerarPdf(credencial.id)

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'PDF regenerado correctamente.',

              'success'

            );

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              'No fue posible regenerar el PDF.',

              'error'

            );

          }

        });

    });

  }

    //==========================================
  // VALIDAR CREDENCIAL
  //==========================================

  validar(credencial: Credencial): void {

    Swal.fire({

      title: '¿Validar credencial?',

      text: 'La credencial cambiará a estado VALIDADA.',

      icon: 'question',

      showCancelButton: true,

      confirmButtonText: 'Validar',

      cancelButtonText: 'Cancelar'

    }).then(result => {

      if (!result.isConfirmed) {

        return;

      }

      this.credencialService

        .validar(credencial.id)

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'Credencial validada correctamente.',

              'success'

            );

            this.cargarDashboard();

            this.cargarCredenciales();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              'No fue posible validar la credencial.',

              'error'

            );

          }

        });

    });

  }

  //==========================================
  // CANCELAR CREDENCIAL
  //==========================================

  cancelar(credencial: Credencial): void {

    Swal.fire({

      title: '¿Cancelar credencial?',

      text: 'La credencial dejará de estar activa.',

      icon: 'warning',

      showCancelButton: true,

      confirmButtonText: 'Cancelar credencial',

      cancelButtonText: 'Cerrar'

    }).then(result => {

      if (!result.isConfirmed) {

        return;

      }

      this.credencialService

        .cancelar(credencial.id)

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'Credencial cancelada.',

              'success'

            );

            this.cargarDashboard();

            this.cargarCredenciales();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              'No fue posible cancelar la credencial.',

              'error'

            );

          }

        });

    });

  }

  //==========================================
  // ACTIVAR CREDENCIAL
  //==========================================

  activar(credencial: Credencial): void {

    Swal.fire({

      title: '¿Activar credencial?',

      text: 'La credencial volverá al estado ACTIVA.',

      icon: 'question',

      showCancelButton: true,

      confirmButtonText: 'Activar',

      cancelButtonText: 'Cancelar'

    }).then(result => {

      if (!result.isConfirmed) {

        return;

      }

      this.credencialService

        .activar(credencial.id)

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'Credencial activada correctamente.',

              'success'

            );

            this.cargarDashboard();

            this.cargarCredenciales();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              'No fue posible activar la credencial.',

              'error'

            );

          }

        });

    });

  }

    //==========================================
  // REGENERAR CREDENCIAL
  //==========================================

  regenerar(credencial: Credencial): void {

    Swal.fire({

      title: '¿Regenerar credencial?',

      html: `
        Se generará un nuevo folio, QR y código de barras.<br><br>
        <b>La credencial anterior será eliminada.</b>
      `,

      icon: 'warning',

      showCancelButton: true,

      confirmButtonText: 'Regenerar',

      cancelButtonText: 'Cancelar'

    }).then(result => {

      if (!result.isConfirmed) {

        return;

      }

      this.credencialService

        .regenerar(credencial.id)

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'La credencial fue regenerada correctamente.',

              'success'

            );

            this.cargarDashboard();

            this.cargarCredenciales();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              'No fue posible regenerar la credencial.',

              'error'

            );

          }

        });

    });

  }

  

  //==========================================
  // VER HISTORIAL
  //==========================================

  verHistorial(credencial: Credencial): void {

    this.credencialSeleccionada = credencial;

    this.credencialService

      .historial(credencial.id)

      .subscribe({

        next: (resp: any) => {

          this.historial = resp;

          this.mostrarHistorial = true;

        },

        error: (err) => {

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible cargar el historial.',

            'error'

          );

        }

      });

  }

  //==========================================
  // CERRAR HISTORIAL
  //==========================================

  cerrarHistorial(): void {

    this.mostrarHistorial = false;

    this.historial = [];

    this.credencialSeleccionada = null;

  }

    //==========================================
  // COLOR DEL ESTADO
  //==========================================

  obtenerClaseEstado(
      estado: string
  ): string {

    switch (estado) {

      case 'ACTIVA':
        return 'badge badge-success';

      case 'VALIDADA':
        return 'badge badge-primary';

      case 'CANCELADA':
        return 'badge badge-danger';

      default:
        return 'badge badge-secondary';

    }

  }

  //==========================================
  // FORMATEAR FECHA
  //==========================================

  formatearFecha(
      fecha: string
  ): string {

    if (!fecha) {

      return '';

    }

    return new Date(fecha)

      .toLocaleDateString(

        'es-MX',

        {

          day: '2-digit',

          month: '2-digit',

          year: 'numeric',

          hour: '2-digit',

          minute: '2-digit'

        }

      );

  }

  //==========================================
  // OBTENER FOTO
  //==========================================

  obtenerFoto(
      credencial: Credencial
  ): string {

    if (

      credencial.alumno?.fotografia?.ruta

    ) {

      return `${window.location.origin}/${credencial.alumno.fotografia.ruta}`;

    }

    return 'assets/img/user.png';

  }

  //==========================================
  // TRACK BY
  //==========================================

  trackByCredencial(
      index: number,
      item: Credencial
  ): number {

    return item.id;

  }

  //==========================================
// CERRAR MODAL
//==========================================

cerrarModal(): void {

  this.credencialSeleccionada = null;

}

}