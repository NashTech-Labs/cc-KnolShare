import { Component, ViewChild, OnInit } from '@angular/core';
import { NavController } from 'ionic-angular';
import {LoginPage} from '../login/login';
import {SignupPage} from '../signup/signup';
import { Nav } from 'ionic-angular';
import {SharedService} from "../../services/shared.service";


@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage implements OnInit{

  isLoggedIn: boolean;
  userName: string;
  pages = {
    'Login': LoginPage ,
    'Signup': SignupPage
  };

  constructor(public navCtrl: NavController, private sharedService: SharedService) {}

  ngOnInit() {
    this.isLoggedIn = this.sharedService.isLoggedIn;
    if(this.isLoggedIn) {
      this.userName = JSON.parse(localStorage.getItem('user')).userName;
    }
  }

  goToPage(page: string) {
    this.navCtrl.push(this.pages[page]);
  }

  logout() {
    this.isLoggedIn = false;
    this.sharedService.logout();
  }
}
