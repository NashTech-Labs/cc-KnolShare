export class SignupForm {
  email : string;
  password : string;
  confirmPassword : string;
  firstName : string;
  middleName : string;
  lastName : string;
  phone : number;

  constructor() {
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
    this.firstName = '';
    this.lastName = '';
    this.middleName = '';
    this.phone = null;
  }
}
