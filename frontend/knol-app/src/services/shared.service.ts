import {Injectable} from "@angular/core";

@Injectable()
export class SharedService {
  isLoggedIn: boolean = false;

  constructor() {
    if (localStorage.getItem("user") && localStorage.getItem("accessToken")) {
      this.isLoggedIn = true;
    }
  }

  logout() {
    this.isLoggedIn = false;
    localStorage.clear();
  }
}
