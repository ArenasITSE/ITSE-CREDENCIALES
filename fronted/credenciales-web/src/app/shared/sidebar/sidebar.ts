import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.css']
})
export class Sidebar {

  private router = inject(Router);

  abierto = true;

  toggleSidebar() {

    this.abierto = !this.abierto;

  }

  cerrarSesion() {

    localStorage.clear();

    this.router.navigate(['/login']);

  }

}