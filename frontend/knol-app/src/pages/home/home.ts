import { Component, ViewChild } from '@angular/core';
import { NavController } from 'ionic-angular';
import {LoginPage} from '../login/login';
import {SignupPage} from '../signup/signup';
import { Nav } from 'ionic-angular';


@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  pages = {
    'Login': LoginPage ,
    'Signup': SignupPage
  };

  constructor(public navCtrl: NavController) {}

  goToPage(page: string) {
    this.navCtrl.push(this.pages[page]);
  }

}
