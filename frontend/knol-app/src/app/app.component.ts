import { Component, ViewChild, OnInit } from "@angular/core";
import { Nav, Platform } from "ionic-angular";
import { StatusBar } from "@ionic-native/status-bar";
import { SplashScreen } from "@ionic-native/splash-screen";

import { HomePage } from "../pages/home/home";
import { LoginPage } from "../pages/login/login";
import { SignupPage } from "../pages/signup/signup";
import {SharedService} from "../services/shared.service";

@Component({
  templateUrl: "app.html"
})
export class MyApp implements OnInit{
  @ViewChild(Nav) nav: Nav;

  rootPage: any = HomePage;
  isLoggedIn: boolean = false;
  pages: Array<{title: string, component: any}>;

  constructor(public platform: Platform,
              public statusBar: StatusBar,
              public sharedService: SharedService,
              public splashScreen: SplashScreen) {
    this.initializeApp();

    // used for an example of ngFor and navigation
    this.pages = [
      { title: "Home", component: HomePage },
      { title: "Login", component: LoginPage },
      { title: "Signup", component: SignupPage }
    ];

  }

  ngOnInit() {
    this.isLoggedIn = this.sharedService.isLoggedIn;
  }

  logout() {
    this.sharedService.logout().subscribe((data: any) => {
      this.sharedService.clearStorage();
      this.isLoggedIn = false;
    }, (err: any) => {
      console.error(err);
    })
  }

  initializeApp() {
    this.platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      this.statusBar.styleDefault();
      this.splashScreen.hide();
    });
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn"t want the back button to show in this scenario
    this.nav.setRoot(page.component);
  }
}
