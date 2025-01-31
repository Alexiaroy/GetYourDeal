import { Injectable } from '@angular/core';

const TOKEN = 's_token';
const USER = 's_user';

@Injectable({
  providedIn: 'root',
})
export class UserStorageService {
  constructor() {}

  public saveToken(token: string) {
    window.localStorage.removeItem(TOKEN);
    console.log('le token est : ' + token);
    window.localStorage.setItem(TOKEN, token);
  }

  static getToken(): string | null {
    return window.localStorage.getItem(TOKEN);
  }

  public saveUser(user: any): void {
    window.localStorage.removeItem(USER);
    console.log('le user est : ' + user);
    window.localStorage.setItem(USER, JSON.stringify(user));
  }

  static getUser(): any {
    const user = window.localStorage.getItem(USER);
    if (user) {
      return JSON.parse(user);
    }
    return {};
  }

  static getUserId(): string {
    const user = this.getUser();
    if (user) {
      return user.userId;
    }
    return '';
  }

  static getUserRole(): string {
    const user = this.getUser();
    if (user) {
      return user.role;
    }
    return '';
  }

  static isClientLoggedIn(): boolean {
    if (this.getToken() === null) {
      return false;
    }
    const role = this.getUserRole();
    return role == 'CLIENT';
  }

  static isCompanyLoggedIn(): boolean {
    if (this.getToken() === null) {
      return false;
    }
    const role = this.getUserRole();
    return role == 'COMPANY';
  }

  static signOut(): void {
    window.localStorage.removeItem(TOKEN);
    window.localStorage.removeItem(USER);
  }
}
