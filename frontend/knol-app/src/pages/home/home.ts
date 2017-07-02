import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {LoginPage} from "../login/login";
import {SignupPage} from "../signup/signup";
import {YoutubePage} from "../youtube/youtube";
import {SharedService} from "../../services/shared.service";


@Component({
  selector: "page-home",
  templateUrl: "home.html"
})
export class HomePage implements OnInit {

  userName: string;
  pages = {
    "Login": LoginPage,
    "Signup": SignupPage,
    "Youtube": YoutubePage
  };

  constructor(public navCtrl: NavController, private sharedService: SharedService) {}

  ngOnInit() {
    if (this.sharedService.isLoggedIn) {
      this.userName = JSON.parse(localStorage.getItem("user")).userName;
    }
  }

  goToPage(page: string) {
    this.navCtrl.push(this.pages[page]);
  }

}
