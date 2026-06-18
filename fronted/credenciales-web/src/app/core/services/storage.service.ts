import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private readonly TOKEN_KEY = 'token';

  guardarToken(token: string): void {

    localStorage.setItem(this.TOKEN_KEY, token);

  }

  obtenerToken(): string | null {

    return localStorage.getItem(this.TOKEN_KEY);

  }

  eliminarToken(): void {

    localStorage.removeItem(this.TOKEN_KEY);

  }

  estaLogueado(): boolean {

    return this.obtenerToken() !== null;

  }

  logout(): void {

    this.eliminarToken();

  }

  obtenerAuthorization(): string {

    return `Bearer ${this.obtenerToken()}`;

  }

  // ==========================================
  // OBTENER EL ROL DEL TOKEN JWT
  // ==========================================

  obtenerRol(): string | null {

    const token = this.obtenerToken();

    if (!token) {

      return null;

    }

    try {

      const payload = JSON.parse(
        atob(token.split('.')[1])
      );

      return payload.rol;

    } catch {

      return null;

    }

  }

  // ==========================================
  // VALIDAR SI ES ADMINISTRADOR
  // ==========================================

  esAdministrador(): boolean {

    const rol = this.obtenerRol();

    return rol === 'ADMINISTRADOR'
        || rol === 'CONTROL_ESCOLAR';

  }

  // ==========================================
  // VALIDAR SI ES ALUMNO
  // ==========================================

  esAlumno(): boolean {

    return this.obtenerRol() === 'ALUMNO';

  }

}