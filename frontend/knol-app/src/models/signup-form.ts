export class SignupForm {
  userName : string;
  email : string;
  password : string;
  confirmPassword : string;
  phoneNumber : number;

  constructor() {
    this.userName = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
    this.phoneNumber = null;
  }
}
