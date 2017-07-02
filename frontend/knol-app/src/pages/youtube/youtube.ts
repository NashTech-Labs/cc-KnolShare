import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {YoutubeService} from "./youtube.service";

@Component({
  selector: "page-youtube",
  templateUrl: "youtube.html",
  providers: [YoutubeService]
})
export class YoutubePage implements OnInit{

  items: any[];
  youtubeData: any;
  ids: string[] = [];
  constructor(public navCtrl: NavController,
              private youtubeService: YoutubeService) {}

  ngOnInit() {
    this.youtubeService.getVideos().subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      localStorage.setItem('videoData', JSON.stringify(this.youtubeData));
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      console.error(err);
    })
  }

  goToPreviewPage(i) {

  }

  previousPage(){
    this.youtubeService.getVideos(null, this.youtubeData.prevPageToken).subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      if(localStorage.getItem('videoData')) {
        localStorage.removeItem('videoData')
      }
      localStorage.setItem('videoData', JSON.stringify(this.youtubeData));
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      console.error(err);
    })
  }

  nextPage() {
    this.youtubeService.getVideos(this.youtubeData.nextPageToken).subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      if(localStorage.getItem('videoData')) {
        localStorage.removeItem('videoData')
      }
      localStorage.setItem('videoData', JSON.stringify(this.youtubeData));
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      console.error(err);
    })
  }
}
