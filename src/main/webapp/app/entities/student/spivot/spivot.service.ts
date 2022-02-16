import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpHeaders } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class SpivotService {
  constructor(protected http: HttpClient) {}

  async spivotToken(): Promise<any> {
    const formData = new FormData();
    formData.append('auth.login', '{"API User":"web","API Password":"Admin!1"}');
    const logReq = new HttpRequest('POST', 'https://spivot.sqilsoft.by/api/', formData, {
      reportProgress: true,
    });
    return await this.http.request(logReq).toPromise();
  }

  async spivotCreate64(token: string): Promise<any> {
    const formData = new FormData();
    formData.append('file.create', '{"FileType": "csv","PublicId":"file_demo"}');
    const createReq = new HttpRequest('POST', 'https://spivot.sqilsoft.by/api/', formData, {
      reportProgress: true,
      headers: new HttpHeaders({ Authorization: token }),
    });
    return await this.http.request(createReq).toPromise();
  }

  async spivotChek(token: string, processId: any): Promise<any> {
    const formData = new FormData();
    formData.append('file.checkStatus', `{"ProcessId":${JSON.stringify(processId)}}`);
    const createReq = new HttpRequest('POST', 'https://spivot.sqilsoft.by/api/', formData, {
      reportProgress: true,
      headers: new HttpHeaders({ Authorization: token }),
    });
    return await this.http.request(createReq).toPromise();
  }

  async spivotDownload64(token: string, processId: any): Promise<any> {
    const formData = new FormData();
    formData.append('file.getData', `{"ProcessId":${JSON.stringify(processId)}}`);
    const createReq = new HttpRequest('POST', 'https://spivot.sqilsoft.by/api/', formData, {
      reportProgress: true,
      headers: new HttpHeaders({ Authorization: token }),
    });
    return await this.http.request(createReq).toPromise();
  }
}
