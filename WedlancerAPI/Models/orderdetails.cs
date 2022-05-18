namespace WedlancerAPI.Models
{
    public class orderdetails
    {
        public int OrderId { get; set; }
        public string PaymentId { get; set; }
        public double Amount { get; set; }
        public string package { get; set; }
        public string freelancerusername { get; set; }
        public string status { get; set; }

    }
}
