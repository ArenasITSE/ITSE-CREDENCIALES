import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import Swal from 'sweetalert2';

import { AlumnoService } from '../../../core/services/alumno.service';
import { CarreraService } from '../../../core/services/carrera.service';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-alumnosdash',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './alumnosdash.html',
  styleUrl: './alumnosdash.css'
})
export class Alumnosdash implements OnInit {

  private alumnoService = inject(AlumnoService);
  private carreraService = inject(CarreraService);

  private router = inject(Router);

  //==========================================
  // ESTADOS
  //==========================================

  cargando = true;

  procesando = false;

  actualizandoSemestres = false;

  //==========================================
  // DATOS
  //==========================================

  alumnos:any[]=[];

  alumnosFiltrados:any[]=[];

  alumnoSeleccionado:any=null;

  //==========================================
  // MODAL EDITAR
  //==========================================

  mostrarModalEditar = false;

  alumnoEditar:any = {

    id:0,

    nombreCompleto:'',

    matricula:'',

    semestre:1,

    nss:'',

    carrera:{

      id:null,

      nombre:''

    }

  };

  //==========================================
  // FILTROS
  //==========================================

  textoBusqueda='';

  filtroCarrera='';

  filtroSemestre='';

  //==========================================
  // CATÁLOGOS
  //==========================================

  carreras:any[]=[];

  semestres:number[]=[];

  //==========================================
  // PAGINACIÓN
  //==========================================

  paginaActual=1;

  registrosPorPagina=10;

  totalPaginas=1;

  //==========================================
  // MENÚ
  //==========================================

  menuAbierto:number|null=null;

  //==========================================
  // INIT
  //==========================================

  ngOnInit(): void {

    this.cargarCarreras();

    this.cargarAlumnos();

  }

  //==========================================
  // CARGAR ALUMNOS
  //==========================================

  cargarAlumnos(): void {

    this.cargando = true;

    this.alumnoService.listar()

      .subscribe({

        next: (resp:any[]) => {

          this.alumnos = resp;

          this.obtenerSemestres();

          this.filtrar();

          this.cargando = false;

        },

        error: (err) => {

          console.error(err);

          this.cargando = false;

          Swal.fire(

            'Error',

            'No fue posible cargar los alumnos.',

            'error'

          );

        }

      });

  }

    //==========================================
  // ACTUALIZAR SEMESTRES GLOBALMENTE
  //==========================================

  
  actualizarSemestresGlobalmente(): void {

    if (this.actualizandoSemestres) {
      return;
    }

    let segundosRestantes = 5;

    let intervalo: ReturnType<typeof setInterval> | undefined;

    Swal.fire({

      title: 'Confirmar actualización de semestres global',

      html: `
        <p>
          Esta operación modificará el semestre de todos los alumnos
          incluidos en el sistema, pasándolos al siguiente automáticamente.
        </p>

        <p>
          <strong>No se puede revertir automáticamente.</strong>
          Por favor, asegúrese de realizar la operación cada semestre,
          una vez verificado el estado académico de cada alumno del ITSE.
        </p>

        <p style="color: #dc2626; font-weight: bold;">
          Los alumnos que deban cursar 10.º semestre o uno superior
          deberán actualizarse manualmente, ya que son casos excepcionales.
        </p>
      `,

      icon: 'warning',

      showCancelButton: true,

      confirmButtonText: 'Confirmar actualización',

      cancelButtonText: 'Cancelar',

      confirmButtonColor: '#d97706',

      cancelButtonColor: '#6b7280',

      reverseButtons: true,

      focusCancel: true,

      confirmButtonAriaLabel: 'Confirmar actualización',

      
didOpen: () => {

    const botonConfirmar = Swal.getConfirmButton();

    const popup = Swal.getPopup();

    if (!botonConfirmar || !popup) {
        return;
    }

    botonConfirmar.disabled = true;

    const contador = document.createElement('p');

    contador.id = 'contador-confirmacion';

    contador.style.cssText = `
        color: #d97706;
        font-weight: bold;
        margin: 0 0 12px;
        text-align: center;
    `;

    contador.textContent =
        `Podrás confirmar en ${segundosRestantes} segundos`;

    const acciones = popup.querySelector('.swal2-actions');

    if (acciones) {
        popup.insertBefore(contador, acciones);
    }

    intervalo = window.setInterval(() => {

        segundosRestantes--;

        if (segundosRestantes > 0) {

            contador.textContent =
                `Podrás confirmar en ${segundosRestantes} segundos`;

        } else {

            if (intervalo !== undefined) {
                window.clearInterval(intervalo);
            }

            botonConfirmar.disabled = false;

            contador.textContent =
                'Ya puedes confirmar la actualización.';

            botonConfirmar.focus();

        }

    }, 1000);

        },

        willClose: () => {

            if (intervalo !== undefined) {
                window.clearInterval(intervalo);
            }

        },


    }).then(result => {

      if (!result.isConfirmed) {
        return;
      }

      this.actualizandoSemestres = true;

      Swal.fire({

        title: 'Actualizando semestres',

        text: 'Por favor, espere mientras se procesa la operación.',

        allowOutsideClick: false,

        allowEscapeKey: false,

        didOpen: () => {
          Swal.showLoading();
        }

      });

      this.alumnoService
        .actualizarSemestresGlobalmente()
        .subscribe({

          next: (respuesta: string) => {

            this.actualizandoSemestres = false;

            Swal.fire({

              title: 'Actualización completada',

              text: respuesta,

              icon: 'success',

              confirmButtonText: 'Aceptar'

            });

            this.cargarAlumnos();

          },

          error: (err) => {

            console.error(err);

            this.actualizandoSemestres = false;

            Swal.fire({

              title: 'Error',

              text: 'No fue posible actualizar los semestres. Compruebe la conexión y los permisos de administrador.',

              icon: 'error',

              confirmButtonText: 'Aceptar'

            });

          }

        });

    }).catch(error => {

        console.error('Error en la ventana de confirmación:', error);

    });

  }


  //==========================================
  // OBTENER SEMESTRES
  //==========================================

  obtenerSemestres(): void {

    this.semestres = [

      ...new Set(

        this.alumnos

          .map(a => a.semestre)

          .filter(Boolean)

      )

    ].sort((a,b)=>a-b);

  }

  //==========================================
  // FILTRAR
  //==========================================

  filtrar(): void {

    let datos = [...this.alumnos];

    //=============================
    // BUSCADOR
    //=============================

    if(this.textoBusqueda.trim()){

      const texto =

        this.textoBusqueda

        .toLowerCase();

      datos = datos.filter(a =>

        a.nombreCompleto

          ?.toLowerCase()

          .includes(texto)

        ||

        a.matricula

          ?.toLowerCase()

          .includes(texto)

        ||

        a.carrera?.nombre

          ?.toLowerCase()

          .includes(texto)

      );

    }

    //=============================
    // CARRERA
    //=============================

    if(this.filtroCarrera){

      datos = datos.filter(

        a =>

          a.carrera?.nombre ===

          this.filtroCarrera

      );

    }

    //=============================
    // SEMESTRE
    //=============================

    if(this.filtroSemestre){

      datos = datos.filter(

        a =>

          a.semestre ==

          this.filtroSemestre

      );

    }

    this.alumnosFiltrados = datos;

    this.calcularPaginas();

  }

  //==========================================
  // PAGINACIÓN
  //==========================================

  calcularPaginas(): void {

    this.totalPaginas = Math.ceil(

      this.alumnosFiltrados.length /

      this.registrosPorPagina

    );

    if(this.totalPaginas===0){

      this.totalPaginas=1;

    }

    if(this.paginaActual>this.totalPaginas){

      this.paginaActual=this.totalPaginas;

    }

  }

  //==========================================
  // ALUMNOS DE LA PÁGINA
  //==========================================

  get alumnosPagina(): any[] {

    const inicio =

      (this.paginaActual-1)

      *

      this.registrosPorPagina;

    const fin =

      inicio +

      this.registrosPorPagina;

    return this.alumnosFiltrados.slice(

      inicio,

      fin

    );

  }

  //==========================================
  // CAMBIAR PÁGINA
  //==========================================

  cambiarPagina(

      pagina:number

  ):void{

    if(

      pagina<1 ||

      pagina>this.totalPaginas

    ){

      return;

    }

    this.paginaActual=pagina;

  }

  //==========================================
  // MENÚ DE ACCIONES
  //==========================================

  abrirMenu(id:number):void{

    if(this.menuAbierto===id){

      this.menuAbierto=null;

      return;

    }

    this.menuAbierto=id;

  }

  cerrarMenu():void{

    this.menuAbierto=null;

  }

  //==========================================
  // NUEVO ALUMNO
  //==========================================

  nuevoAlumno():void{

    this.router.navigate([

      '/nuevo-alumno'

    ]);

  }

  //==========================================
  // EDITAR
  //==========================================

  //==========================================
  // ABRIR MODAL EDITAR
  //==========================================

  editar(alumno:any):void{

    this.alumnoEditar = {

      id: alumno.id,

      nombreCompleto: alumno.nombreCompleto,

      matricula: alumno.matricula,

      semestre: alumno.semestre,

      nss: alumno.nss || '',

      carrera:{

        id: alumno.carrera.id,

        nombre: alumno.carrera.nombre

      }

    };

    this.mostrarModalEditar = true;

    this.cerrarMenu();

  }

  //==========================================
  // CERRAR MODAL
  //==========================================

  cerrarModalEditar():void{

    this.mostrarModalEditar=false;

  }

  //==========================================
  // GUARDAR CAMBIOS
  //==========================================

  guardarEdicion():void{

    this.procesando=true;

    const datos={

      nombreCompleto:this.alumnoEditar.nombreCompleto,

      matricula:this.alumnoEditar.matricula,

      semestre:this.alumnoEditar.semestre,

      nss:this.alumnoEditar.nss,

      carreraId:this.alumnoEditar.carrera.id

    };

    this.alumnoService

      .actualizar(

        this.alumnoEditar.id,

        datos

      )

      .subscribe({

        next:()=>{

          this.procesando=false;

          this.mostrarModalEditar=false;

          Swal.fire(

            'Correcto',

            'Alumno actualizado correctamente.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          this.procesando=false;

          Swal.fire(

            'Error',

            'No fue posible actualizar el alumno.',

            'error'

          );

        }

      });

  }

  //==========================================
  // CANCELAR EDICIÓN
  //==========================================

  cancelarEdicion():void{

    this.mostrarModalEditar=false;

  }

  //==========================================
  // ELIMINAR ALUMNO
  //==========================================

  eliminarAlumno(alumno:any):void{

    Swal.fire({

      title:'¿Eliminar alumno?',

      text:'Se eliminará el alumno, usuario, fotografía y credencial.',

      icon:'warning',

      showCancelButton:true,

      confirmButtonText:'Eliminar',

      cancelButtonText:'Cancelar',

      confirmButtonColor:'#dc2626'

    }).then(result=>{

      if(!result.isConfirmed){

        return;

      }

      this.alumnoService

      .eliminar(alumno.id)

      .subscribe({

        next:()=>{

          Swal.fire(

            'Correcto',

            'Alumno eliminado.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible eliminar el alumno.',

            'error'

          );

        }

      });

      }).catch(error => {

      console.error('Error en la confirmación de eliminación:', error);

    });
      

  }

  //==========================================
  // ELIMINAR CREDENCIAL
  //==========================================

  eliminarCredencial(alumno:any):void{

    Swal.fire({

      title:'¿Eliminar credencial?',

      text:'La credencial será eliminada definitivamente.',

      icon:'warning',

      showCancelButton:true,

      confirmButtonText:'Eliminar',

      cancelButtonText:'Cancelar',

      confirmButtonColor:'#dc2626'

    }).then(result=>{

      if(!result.isConfirmed){

        return;

      }

      this.alumnoService

      .eliminarCredencial(alumno.id)

      .subscribe({

        next:()=>{

          Swal.fire(

            'Correcto',

            'Credencial eliminada.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible eliminar la credencial.',

            'error'

          );

        }

      });

      }).catch(error => {

      console.error('Error en la confirmación de eliminación de credencial:', error);

    });


  }

  //==========================================
  // CANCELAR CREDENCIAL
  //==========================================

  cancelarCredencial(alumno:any):void{

    this.alumnoService

      .cancelarCredencial(alumno.id)

      .subscribe({

        next:()=>{

          Swal.fire(

            'Correcto',

            'Credencial cancelada.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible cancelar la credencial.',

            'error'

          );

        }

      });

  }

  //==========================================
  // ACTIVAR CREDENCIAL
  //==========================================

  activarCredencial(alumno:any):void{

    this.alumnoService

      .activarCredencial(alumno.id)

      .subscribe({

        next:()=>{

          Swal.fire(

            'Correcto',

            'Credencial activada.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible activar la credencial.',

            'error'

          );

        }

      });

  }

  //==========================================
  // VALIDAR CREDENCIAL
  //==========================================

  validarCredencial(alumno:any):void{

    this.alumnoService

      .validarCredencial(alumno.id)

      .subscribe({

        next:()=>{

          Swal.fire(

            'Correcto',

            'Credencial validada correctamente.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible validar la credencial.',

            'error'

          );

        }

      });

  }

  //==========================================
  // REGENERAR CREDENCIAL
  //==========================================

  regenerarCredencial(alumno:any):void{

    Swal.fire({

      title:'¿Regenerar credencial?',

      text:'Se generará un nuevo folio y un nuevo código QR.',

      icon:'question',

      showCancelButton:true,

      confirmButtonText:'Regenerar',

      cancelButtonText:'Cancelar',

      confirmButtonColor:'#2563eb'

    }).then(result=>{

      if(!result.isConfirmed){

        return;

      }

      this.alumnoService

        .regenerarCredencial(alumno.id)

        .subscribe({

          next:()=>{

            Swal.fire(

              'Correcto',

              'Credencial regenerada.',

              'success'

            );

            this.cargarAlumnos();

          },

          error:(err)=>{

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
  // RESTABLECER PASSWORD
  //==========================================

  restablecerPassword(alumno:any):void{

    Swal.fire({

      title:'¿Restablecer contraseña?',

      text:'Se generará una nueva contraseña temporal.',

      icon:'question',

      showCancelButton:true,

      confirmButtonText:'Restablecer',

      cancelButtonText:'Cancelar'

    }).then(result=>{

      if(!result.isConfirmed){

        return;

      }

      this.alumnoService

        .restablecerPassword(alumno.id)

        .subscribe({

          next:(resp:string)=>{

            Swal.fire({

              title:'Contraseña restablecida',

              html:`

                <p>La nueva contraseña es:</p>

                <h2 style="color:#2563eb">${resp}</h2>

              `,

              icon:'success',

              confirmButtonText:'Copiar'

            });

          },

          error:(err)=>{

            console.error(err);

            Swal.fire(

              'Error',

              'No fue posible restablecer la contraseña.',

              'error'

            );

          }

        });

    });

  }

  //==========================================
  // CAMBIAR FOTO
  //==========================================

  cambiarFoto(

      alumno:any,

      event:any

  ):void{

    const archivo =

      event.target.files[0];

    if(!archivo){

      return;

    }

    this.alumnoService

      .cambiarFoto(

        alumno.id,

        archivo

      )

      .subscribe({

        next:()=>{

          Swal.fire(

            'Correcto',

            'Fotografía actualizada.',

            'success'

          );

          this.cargarAlumnos();

        },

        error:(err)=>{

          console.error(err);

          Swal.fire(

            'Error',

            'No fue posible actualizar la fotografía.',

            'error'

          );

        }

      });

  }

  //==========================================
  // TRACKBY
  //==========================================

  trackByAlumno(

      index:number,

      alumno:any

  ):number{

    return alumno.id;

  }

  //==========================================
  // CARGAR CARRERAS
  //==========================================

  cargarCarreras(): void {

    this.carreraService

      .listar()

      .subscribe({

        next: (resp) => {

          this.carreras = resp;

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  //==========================================
  // URL FOTO
  //==========================================

  obtenerFoto(alumno:any):string{

    if(

      !alumno.fotografia ||

      !alumno.fotografia.ruta

    ){

      return '';

    }

    return environment.apiUrl.replace(

      '/api',

      ''

    ) + '/' +

    alumno.fotografia.ruta;

  }

}