export interface Dashboard {

  totalAlumnos: number;

  credencialesActivas: number;

  credencialesCanceladas: number;

  totalCarreras: number;

  ultimoAlumno: string;

  ultimaCredencial: string;

  ultimaActualizacion: string;

  alumnosPorCarrera: CarreraDashboard[];

  alumnosPorSemestre: SemestreDashboard[];

  credencialesPorEstado: EstadoDashboard[];

  ultimosAlumnos: UltimoAlumno[];

  ultimasCredenciales: UltimaCredencial[];

}

export interface CarreraDashboard {

  nombre: string;

  total: number;

}

export interface SemestreDashboard {

  semestre: number;

  total: number;

}

export interface EstadoDashboard {

  estado: string;

  total: number;

}

export interface UltimoAlumno {

  nombre: string;

  matricula: string;

  semestre: number;

}

export interface UltimaCredencial {

  nombre: string;

  folio: string;

  fecha: string;

}