import { Injectable } from '@angular/core';
import { SpivotService } from './spivot.service';
import { saveAs } from 'file-saver';

export interface IAuthorization {
  code?: number;
  result?: {
    token?: string;
  };
}

@Injectable({ providedIn: 'root' })
export class Spivot {
  token: string | undefined = '';

  constructor(protected spivotService: SpivotService) {}

  loginRequest(): any {
    this.spivotService.spivotToken().then(response => {
      const token = response.body.result.token;
      this.spivitCreate64(token);
    });

    return 'this.spivotService.spivotdownload()';
  }

  async spivitCreate64(token: string): Promise<any> {
    return await this.spivotService.spivotCreate64(token).then(response => {
      const processId = response.body.result.processId;
      this.spivitChek(token, processId);
    });
  }

  async spivitChek(token: string, processId: any): Promise<any> {
    return await this.spivotService.spivotChek(token, processId).then(response => {
      const statusChek = response.body.result.fileIsExist;
      if (statusChek !== 'true') {
        setTimeout(() => {
          this.spivitChek(token, processId);
        }, 1000);
      } else {
        this.spivitDowload64(token, processId);
      }
    });
  }

  async spivitDowload64(token: string, processId: any): Promise<any> {
    return await this.spivotService.spivotDownload64(token, processId).then(response => {
      const fileB64 = response.body.result.data.data;
      const typeFile = response.body.result.data.type;
      const decod = atob(fileB64);
      const file = new File([decod], 'test.csv');
      saveAs(file);
    });
  }
}
