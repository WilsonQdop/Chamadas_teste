import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CalledService } from '../../services/called.service';
import { CommonModule } from '@angular/common';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
  selector: 'app-create-called',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NavBarComponent],
  templateUrl: './create-called.component.html',
  styleUrl: './create-called.component.scss'
})
export class CreateCalledComponent {
  calledForm: FormGroup;
  loading = false;

  constructor(private calledService: CalledService) {
    this.calledForm = new FormGroup({
      title: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
      category: new FormControl('LOW', [Validators.required])
    });
  }

  onSubmit() {
    if (this.calledForm.valid) {
      this.loading = true;
      this.calledService.create(this.calledForm.value).subscribe({
        next: (res) => {
          alert('Chamado criado com sucesso!');
          this.calledForm.reset({ category: 'LOW' });
          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          alert('Erro ao criar chamado. Verifique se seu token ainda é válido.');
          this.loading = false;
        }
      });
    }
  }
}