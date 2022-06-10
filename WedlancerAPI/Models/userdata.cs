namespace WedlancerAPI.Models
{
    public class userdata
    {
        public int Id { get; set; }
        public string ProfilePicture { get; set; }
        public string UserName { get; set; }
        public string firstname { get; set; }
        public string lastname { get; set; }
        public string Email { get; set; }
        public long phonenumber { get; set; }
        public string city { get; set; }
        public string state { get; set; }

        public string category { get; set; }

        public bool IsActive { get; set; }


    }
}
