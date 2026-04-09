import { Component } from '@angular/core';
import { DefaultLoginLayoutComponent } from '../../components/default-login-layout/default-login-layout.component';
import { Form, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PrimaryInputComponent } from '../../components/primary-input/primary-input.component';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    DefaultLoginLayoutComponent,
    ReactiveFormsModule,
    PrimaryInputComponent,
    FormsModule
  ],
  providers: [LoginService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm!: FormGroup;

  constructor(
    private router: Router,
    private loginService: LoginService
  ) {
    sessionStorage.removeItem('auth-token');
    
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(3)])
    });
  }

  submit() {
    if (this.loginForm.valid) {
      // Extraímos os valores do formulário (email e password)
      const { email, password } = this.loginForm.value;

      this.loginService.login(email, password).subscribe({
        next: (response) => {
          // Log para confirmar o token no console do navegador (F12)
          console.log('✅ Login realizado com sucesso!');
          console.log('Token JWT:', sessionStorage.getItem('auth-token'));
          
          alert('Logado com sucesso!');

          // Redireciona para a tela de backup que criamos
          this.router.navigate(['/backup']); 
        },
        error: (err) => {
          console.error('❌ Erro no login:', err);
          alert('Erro ao logar: Email ou senha inválidos');
        }
      });
    } else {
      alert('Por favor, preencha o formulário corretamente.');
    }
  }

  navigate() {
    this.router.navigate(['cadastro']);
  }
}