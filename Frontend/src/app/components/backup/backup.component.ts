import { Component, OnInit } from '@angular/core'; // Importamos OnInit
import { BackupService } from '../../services/backup-service.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
  selector: 'app-backup',
  standalone: true, 
  imports: [CommonModule, FormsModule, NavBarComponent],
  templateUrl: './backup.component.html',
  styleUrl: './backup.component.scss'
})
export class BackupComponent implements OnInit { // Implementamos a interface
  mensagem: string = '';
  carregando: boolean = false;
  listaBackups: string[] = [];
  arquivoSelecionado: string = ''; // Agora usamos esta para o select

  constructor(private backupService: BackupService) {}

  // Carrega a lista assim que o componente é iniciado
  ngOnInit(): void {
    this.carregarLista();
  }

  carregarLista(): void {
    this.backupService.listarArquivos().subscribe({
      next: (files) => {
        this.listaBackups = files;
      },
      error: (err) => {
        console.error('Erro ao carregar lista de backups', err);
      }
    });
  }

  onBackup() {
    this.carregando = true;
    this.mensagem = 'Iniciando backup...';
    
    this.backupService.executarBackup().subscribe({
      next: (res) => {
        this.mensagem = res;
        this.carregando = false;
        this.carregarLista(); // Atualiza a lista para o novo backup aparecer
        alert('Backup realizado com sucesso!');
      },
      error: (err) => {
        console.error(err);
        this.mensagem = 'Erro ao realizar backup.';
        this.carregando = false;
      }
    });
  }

  onRestore() {
    if (!this.arquivoSelecionado) {
      alert('Por favor, selecione um arquivo de backup na lista!');
      return;
    }

    if (confirm(`ATENÇÃO: Deseja restaurar o backup "${this.arquivoSelecionado}"? Dados atuais serão perdidos!`)) {
      this.carregando = true;
      this.mensagem = 'Restaurando banco de dados...';

      this.backupService.restaurarBackup(this.arquivoSelecionado).subscribe({
        next: (res) => {
          this.mensagem = res;
          this.carregando = false;
          alert('Restauração concluída com sucesso!');
        },
        error: (err) => {
          console.error(err);
          this.mensagem = 'Erro no restore: ' + (err.error || 'Verifique o console');
          this.carregando = false;
        }
      });
    }
  }
}