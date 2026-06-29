export interface Credencial {

  id: number;

  folio: string;

  codigoQR: string;

  codigoBarras: string;

  qrPath: string;

  estado: string;

  fechaGeneracion: string;

  fechaVencimiento?: string;

  alumno: {

    id: number;

    nombreCompleto: string;

    matricula: string;

    semestre: number;

    carrera: {

      id: number;

      nombre: string;

    };

    fotografia: {

      id: number;

      ruta: string;

    };

  };

}