import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {YoutubeService} from "./youtube.service";
import {YoutubePreviewPage} from "./youtube-preview";

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
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      alert(err);
      console.error(err);
    })
  }

  goToPreviewPage(i: number) {
    if(localStorage.getItem('videoData')) {
      localStorage.removeItem('videoData')
    }
    localStorage.setItem('videoData', JSON.stringify(this.items[i]));
    this.navCtrl.push(YoutubePreviewPage);
  }

  previousPage(){
    this.youtubeService.getVideos(null, this.youtubeData.prevPageToken).subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      alert(err);
      console.error(err);
    })
  }

  nextPage() {
    this.youtubeService.getVideos(this.youtubeData.nextPageToken).subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      alert(err);
      console.error(err);
    })
  }
}
